package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/5 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONArray;
import com.example.demo.analyzer.AdaAnalyzer;
import com.example.demo.analyzer.AnalyzerDatabase;
import com.example.demo.config.exception.BusinessException;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.dto.ReportDto;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectVersionEntity;
import com.example.demo.service.*;
import com.example.demo.task.ProduceReportTask;
import com.example.demo.util.CompressUtil;
import com.example.demo.util.MyFileUtil;
import com.example.demo.util.MyStringUtils;
import com.example.demo.util.UUIDUtils;
import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping(value = "file-upload")
public class FileUploadController {

    @Value("${pretreatmentPath}")
    private String PRETREATMENT_PATH;
    @Value("${uploadPath}")
    private String path;
    @Value("${reportPath}")
    private String reportPath;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ReportService reportService;

    private Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
//    @Value("${uploadPath.tcf}")
//    private String tcfPath;
//
//    @Value("${uploadPath.coverage}")
//    private String coveragePath;
//    @RequestMapping(value = "/tcf", method = RequestMethod.POST)
//    @ResponseBody
//    public String uploadTcf(HttpServletRequest request, HttpServletResponse response) {
//        return "success";
//    }

    private void excuteUpload(String uploadDir, MultipartFile file) {
        // 文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 上传文件名
        String fileName = UUID.randomUUID() + suffix;
        // 服务器端保存的文件对象
        File serverFile = new File(uploadDir + fileName);
        try {
            file.transferTo(serverFile);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

//    @RequestMapping(value = "/tcf", method = RequestMethod.POST)
//    @ResponseBody
//    public String uploadTcf(HttpServletRequest request, @RequestParam("file[]") MultipartFile[] files) {
//        try {
//            // 上传目录地址
//            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload2/";
//            System.out.println("uploads - uploadDir=" + uploadDir);
//            // 如果目录不存在，自动创建
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//
//            // 遍历文件数组执行上传
//            for (int i = 0; i < files.length; i++) {
//                System.out.println("files[i]=" + files[i]);
//                if (files[i] != null && files[i].getSize() > 0) {
//                    excuteUpload(uploadDir, files[i]);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "上传失败";
//        }
//        return "上传成功";
//    }

    @PostMapping(value = "/checkVersion")
    @ResponseBody
    public List<ProjectVersionEntity> checkVersion(@RequestParam(name = "projectId") Integer projectId,
                           @RequestParam(name = "version") String version){
        return projectService.selectByVersionAndId(projectId,version);
    }

    @PostMapping(value = "/function")
    @ResponseBody
    public String uploadFunctionMultiFiles(MultipartHttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestParam(name = "projectId") Integer projectId,
                                               @RequestParam(name = "projectName") String projectName,
                                               @RequestParam(name = "version") String version)throws IOException, RarException {

        String message="";
        try{
            // Getting uploaded files from the request object
            Map<String, MultipartFile> fileMap = request.getFileMap();

            // Maintain a list to send back the files info. to the client side
            List<FileUpload> uploadedFiles = new ArrayList<FileUpload>();
            String uploadDir = path + UUID.randomUUID().toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Iterate through the map
            for (MultipartFile multipartFile : fileMap.values()) {

                // Save the file to local disk
                //saveFileToLocalDisk(dir ,multipartFile);
                unDo(dir ,multipartFile);
                ProjectVersionEntity projectVersionEntity = new ProjectVersionEntity();
                projectVersionEntity.setProjectId(projectId);
                // projectVersionEntity.setProjectVersion(UUIDUtils.getUUID(40));
                projectVersionEntity.setProjectVersion(version);
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String createPerson = currentUserService.getUser().getUsername();
                if(createPerson == null || createPerson.isEmpty()){
                    return JsonResult.failed(16, "登陆已过期,请重新登陆.");
                }
                projectVersionEntity.setCommits("[{\"author\":{\"name\":\""+createPerson+"\"},\"timestamp\":\""+now+"\"}]");
                projectVersionEntity.setCreateTime(new Date());
                projectService.addProjectVersion(projectVersionEntity);

                AdaAnalyzer.PRETREATMENT_DIR = PRETREATMENT_PATH;
                AnalyzerDatabase db = AdaAnalyzer.analyzeAdaSource(dir.getAbsolutePath());
                procedureService.updateProcedureDatabase(projectId, projectVersionEntity.getProjectVersion(), db);
            }
        }catch (Exception e){
            message = e.getMessage();
            if(message != null && message.contains("uk_sys_project_procedure")){
                Integer id = MyStringUtils.getId(message);
                if(id != -1){
                    ProcedureEntity procedureEntity =  procedureService.findProcedureById(id);
                    message = "提交代码中有重复函数\n"+procedureEntity.getFullName();
                }
            }
            LOGGER.error(e.getMessage());
        }
        if(message.isEmpty()){
            return JsonResult.success();
        }else{
            return JsonResult.failed(16, message);
        }
    }

    @PostMapping(value = "/all")
    @ResponseBody
    public String uploadTcfMultiFiles(MultipartHttpServletRequest request,
                                                HttpServletResponse response,
                                                @RequestParam(name = "project") Integer project,
                                                @RequestParam(name = "version") String version,
                                                @RequestParam(name = "projectName") String projectName)throws IOException, RarException {

        String message = "";
        try{
            String userName = currentUserService.getUser().getUsername();
            if(userName == null || userName.isEmpty()){
                return JsonResult.failed(16, "登陆已过期,请重新登陆.");
            }
            for(int i = 0; i< ProduceReportTask.reportList.size(); i++){
                ReportDto dto = ProduceReportTask.reportList.get(i);
                if(dto.getProjectId() == project &&
                        dto.getProjectVersion().equals(version) &&
                        dto.getCreatePerson().equals(userName)){
                    return JsonResult.failed(16,"报告正在生成中,请稍后再上传.");
                }
            }

            // Getting uploaded files from the request object
            Map<String, MultipartFile> fileMap = request.getFileMap();

            // Maintain a list to send back the files info. to the client side
            List<FileUpload> uploadedFiles = new ArrayList<FileUpload>();
            // Iterate through the map
            List<ProcedureDto> procedureDtoList = procedureService.selectFunctionsByProjectId(project,version);
            File pathFile = new File(path);
            Boolean isSingle = false;
            for (MultipartFile multipartFile : fileMap.values()) {
                // Save the file to local disk
                //saveFileToLocalDisk(dir ,multipartFile);
                String uploadDir = path + UUID.randomUUID().toString();
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File unFile = unDo(dir ,multipartFile);
                if(unFile.isDirectory()){
                    isSingle = false;
                }else if(unFile.isFile()){
                    isSingle = true;
                }
                Integer maxNum = fileUploadService.selectMaxTcfByProjectIdAndVersion(project,version);
                fileUploadService.scanUploadFiles(procedureDtoList, dir.getAbsolutePath(), dir,project,version,maxNum,projectName,pathFile.getAbsolutePath(),isSingle);
                //FileUpload fileInfo = getFileUploadInfo(multipartFile);

                // Save the file info to database
                //fileInfo = saveFileToDatabase(fileInfo);

                // adding the file info to the list
                //uploadedFiles.add(fileInfo);
            }

            String log = "上传版本为“" + version + "”的“" + projectName + "”项目的文件进行解析";
            String createPerson = currentUserService.getUser().getUsername();
            Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logService.insertLog(log, createPerson, createTime);

            //reportService.checkReport(project,version);
        }catch(Exception e){
            message = e.getMessage();
            LOGGER.error(message);
        }
        if(message == null || message.isEmpty()){
            return JsonResult.success();
        }else{
            return JsonResult.failed(16, message);
        }
    }

    @PostMapping(value = "/single")
    @ResponseBody
    public String uploadSigenalFiles(MultipartHttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(name = "project") Integer project,
                                      @RequestParam(name = "version") String version,
                                      @RequestParam(name = "fullName") String fullName,
                                      @RequestParam(name = "hashValue") String hashValue,
                                      @RequestParam(name = "fileType") Integer fileType,
                                      @RequestParam(name = "pictureType") Integer pictureType) {

        String message = "";
        try{
            String userName = currentUserService.getUser().getUsername();
            if(userName == null || userName.isEmpty()){
                return JsonResult.failed(16, "登陆已过期,请重新登陆.");
            }
            for(int i = 0; i< ProduceReportTask.reportList.size(); i++){
                ReportDto dto = ProduceReportTask.reportList.get(i);
                if(dto.getProjectId() == project &&
                        dto.getProjectVersion().equals(version) &&
                        dto.getCreatePerson().equals(userName)){
                    return JsonResult.failed(16,"报告正在生成中,请稍后再上传.");
                }
            }

            // Getting uploaded files from the request object
            Map<String, MultipartFile> fileMap = request.getFileMap();

            // Maintain a list to send back the files info. to the client side
            List<FileUpload> uploadedFiles = new ArrayList<FileUpload>();
            // Iterate through the map
            File pathFile = new File(path);
            for (MultipartFile multipartFile : fileMap.values()) {
                // Save the file to local disk
                //saveFileToLocalDisk(dir ,multipartFile);
                String uploadDir = path + UUID.randomUUID().toString();
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File unFile = unDo(dir ,multipartFile);
                fileUploadService.scanUploadSingleFiles(unFile,project,version,fullName,hashValue,fileType,pictureType,pathFile.getAbsolutePath());
            }

        }catch(Exception e){
            message = e.getMessage();
            LOGGER.error(message);
        }
        if(message == null || message.isEmpty()){
            return JsonResult.success();
        }else{
            return JsonResult.failed(16, message);
        }
    }

    private void saveFileToLocalDisk(File dir, MultipartFile multipartFile) throws IOException {
        String outputFileName = dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename();
        FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(outputFileName));
    }

    private File unDo(File dir, MultipartFile multipartFile) throws IOException, RarException {
        // 文件后缀
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if(".rar".equals(suffix)) {
            try{
                MyFileUtil.unRar(dir, multipartFile);
                return dir;
            }catch (Exception e){
                String msg = e.getMessage();
                LOGGER.error(msg);
                if(msg.contains("5")){
                    throw new BusinessException("只支持版本为5以下的rar");
                }else{
                    throw new BusinessException(msg);
                }
            }
        } else if(".zip".equals(suffix)) {
            MyFileUtil.unZip(dir, multipartFile);
            return dir;
        } else {
            File file = MyFileUtil.unSingle(dir, multipartFile);
            return file;
        }
    }

    private void unDoSingle(File dir, MultipartFile multipartFile) throws IOException, RarException {

    }

    @GetMapping("/downloadReport")
    @ResponseBody
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String fileName = request.getParameter("fileName");// 文件名
        String reportName = request.getParameter("reportName"); //报告名
        if (fileName != null) {
            //设置文件路径
            File file = new File(reportPath+File.separator+fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                String name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1);
                // name=new String(name.getBytes("iso8859-1"),"UTF-8");
                response.setContentType("application/octet-stream");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name, "UTF-8"));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
//                    int i = bis.read(buffer);
//                    while (i != -1) {
//                        os.write(buffer, 0, i);
//                        i = bis.read(buffer);
//                    }

                    int len = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                        if(len < 1024){
                            System.out.println(len);
                        }
                    }
//                    int len = 0;
//                    while((len = bis.read(buffer)) >0){
//                        os.write(buffer, 0, len);
//                    }
                    String log = "下载“" + reportName + "”报告";
                    if(currentUserService.getUser().getId() != 0){
                        String createPerson = currentUserService.getUser().getUsername();
                        Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        logService.insertLog(log, createPerson, createTime);
                    }

                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error(e.getMessage());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (os != null) {
                        try {
                            os.flush();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return "下载失败";
    }

    @GetMapping("/downloadUp")
    @ResponseBody
    public String downloadUpFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String fileName = request.getParameter("fileName");// 文件名
        String reportName = request.getParameter("reportName"); //报告名
        if (fileName != null) {
            //设置文件路径
            File file = new File(path+File.separator+fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                String name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1);
                        // name=new String(name.getBytes("iso8859-1"),"UTF-8");
                if(name.endsWith(".tcf") || name.endsWith(".docx")){
                    response.setContentType("application/octet-stream");
                }else if(name.endsWith(".html") || name.endsWith(".htm")){
                    response.setContentType("text/html");
                }else if(name.endsWith(".jpg") || name.endsWith("jpeg")){
                    response.setContentType("image/jpeg");
                }else if(name.endsWith(".png")){
                    response.setContentType("image/png");
                }
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name, "UTF-8"));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int len = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                        if(len < 1024){
                            System.out.println(len);
                        }
                    }
                    String log = "下载“" + reportName + "”报告";
                    String createPerson = currentUserService.getUser().getUsername();
                    Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    logService.insertLog(log, createPerson, createTime);

                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error(e.getMessage());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (os != null) {
                        try {
                            os.flush();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return "下载失败";
    }

    @GetMapping("/downloadNoChange")
    @ResponseBody
    public String downloadNoChangeFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer project = Integer.valueOf(request.getParameter("project"));
        String version = request.getParameter("version");
        String path = fileUploadService.copyAllFiles(project,version);
        if (path != null && !"".equals(path)) {
            String compressPath = CompressUtil.generateFile(path,"zip");
            //设置文件路径
            File file = new File(compressPath);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                String name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1);
                // name=new String(name.getBytes("iso8859-1"),"UTF-8");
                if(name.endsWith(".rar")){
                    response.setContentType("application/octet-stream");
                    //response.setContentType("application/x-rar-compressed");
                }else if(name.endsWith(".zip")){
                    response.setContentType("application/x-zip-compressed");
                }
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name, "UTF-8"));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int len = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
//                        if(len < 1024){
//                            System.out.println(len);
//                        }
                    }

                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error(e.getMessage());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    if (os != null) {
                        try {
                            os.flush();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LOGGER.error(e.getMessage());
                        }
                    }
                    //删除
                    fileUploadService.deleteDownloadFile(file);
                    fileUploadService.deleteDir(path);
                }
            }
        } else {
            return "没有文件";
        }
        return "下载失败";
    }
}
