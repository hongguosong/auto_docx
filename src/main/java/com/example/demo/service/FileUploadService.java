package com.example.demo.service;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.service </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.config.exception.BusinessException;
import com.example.demo.dao.CoverageFileDao;
import com.example.demo.dao.PictureFileDao;
import com.example.demo.dao.TcfFileDao;
import com.example.demo.dao.WordMubanDao;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.CoverageFileEntity;
import com.example.demo.entity.PictureFileEntity;
import com.example.demo.entity.TcfFileEntity;
import com.example.demo.entity.WordMubanEntity;
import com.example.demo.security.entity.User;
import com.example.demo.util.MyFileUtil;
import com.github.pagehelper.PageHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional
public class FileUploadService {

    @Value("${uploadPath}")
    private String uploadPath;
    @Value("${downloadPath}")
    private String downloadPath;

    @Resource
    private TcfFileDao tcfFileDao;

    @Resource
    private CoverageFileDao coverageFileDao;

    @Resource
    private PictureFileDao pictureFileDao;

    @Resource
    private WordMubanDao wordMubanDao;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ProcedureService procedureService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String findHashValue(String name, List<ProcedureDto> procedureDtoList){
        String hashvalue = "";
        for(ProcedureDto dto: procedureDtoList){
            if(name.equals(dto.getLongName())){
                hashvalue = dto.getHashValue();
            }else{
                String longName = dto.getFullName().toLowerCase().replace("(","_").replace(",","_").replace(")","");
                if(name.equals(longName)){
                    hashvalue = dto.getHashValue();
                }
            }
        }
        return hashvalue;
    }

    /**
     * 解析上传文件
     * @param directory 上传文件目录
     * @return
     */
    public void scanUploadFiles(List<ProcedureDto> procedureDtoList, String base, File directory, Integer projectId, String version, Integer maxNum, String projectName, String path, Boolean isSingle) throws IOException{
        if (directory.isDirectory()) {
            File[] filelist = directory.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    maxNum = selectMaxTcfByProjectIdAndVersion(projectId, version);
                    scanUploadFiles(procedureDtoList, base, filelist[i], projectId, version, maxNum, projectName, path, isSingle);
                }
                else {
                    User user = currentUserService.getUser();
                    Date now = new Date();
                    String fileName = filelist[i].getName();
                    String absPath = filelist[i].getAbsolutePath();

                    String baoPath = absPath.replace(base,"");
                    baoPath = baoPath.substring(0,baoPath.lastIndexOf(File.separator));
                    String baoName = baoPath.replace(File.separator,".").toLowerCase();
                    if(baoName.startsWith(".")){
                        baoName = baoName.substring(1,baoName.length());
                    }
                    System.out.println(baoPath);
                    String hashValue = "";
                    if(fileName.toLowerCase().endsWith(".tcf")){ //根据文件名解析
                        String name = fileName.substring(0, fileName.lastIndexOf(".")).toLowerCase();
                        TcfFileEntity tcfFileEntity = new TcfFileEntity();
                        tcfFileEntity.setFileName(fileName);
                        tcfFileEntity.setFileUrl(filelist[i].getAbsolutePath().replace(path,""));
                        if(isSingle){
                            tcfFileEntity.setFunctionName(name.toLowerCase());
                        }else{
                            tcfFileEntity.setFunctionName(baoName.toLowerCase());
                        }
//                        tcfFileEntity.setProjectId(projectId);
//                        tcfFileEntity.setProjectVersion(version);
                        tcfFileEntity.setProjectId(0);
                        tcfFileEntity.setProjectVersion("");
                        tcfFileEntity.setCreatPerson(user.getUsername());
                        tcfFileEntity.setCreatTime(now);
                        // List<TcfFileEntity> list = tcfFileDao.selectByNameAndProjectIdAndVersion(tcfFileEntity.getFunctionName(), projectId, version);
                        hashValue = findHashValue(tcfFileEntity.getFunctionName(), procedureDtoList);
                        if(hashValue != null && !hashValue.isEmpty()){
                            tcfFileEntity.setHashValue(hashValue);
                            List<TcfFileEntity> list = tcfFileDao.findTcfFileByFuncName(tcfFileEntity.getFunctionName(), hashValue);

                            if(list == null || list.size() == 0){
                                maxNum = maxNum + 1;
                                String tcfNum = projectName+".00_S"+maxNum;
                                tcfFileEntity.setNum(tcfNum);
                                tcfFileDao.insert(tcfFileEntity);
                            }else{
                                tcfFileDao.updateTcfFileByFuncName(tcfFileEntity);
                                deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
                            }
                        }
                    }else if(fileName.toLowerCase().endsWith(".htm") || fileName.toLowerCase().endsWith(".html")){
                        Document doc = Jsoup.parse(filelist[i], "UTF-8");
                        List<Map<String, Object>> result = findResultFromHtml(doc);
                        String compar = baoName.toLowerCase();

                        int k=0;
                        for(; k<result.size(); k++){
                            if(compar.equals(result.get(k).get("name").toString().toLowerCase())){
                                insertCoverage(fileName, filelist[i], path, user, now, baoName, result.get(k),procedureDtoList,isSingle);
                                break;
                            }
                        }

                        if(k == result.size()){
                            findResultFromHtml2(doc, result);
                            compar = compar.substring(compar.lastIndexOf(".")+1, compar.length());
                            for(int m=0; m<result.size(); m++){
                                if(compar.equals(result.get(m).get("longName").toString().toLowerCase())){
                                    insertCoverage(fileName, filelist[i], path, user, now, baoName, result.get(m),procedureDtoList,isSingle);
                                    break;
                                }
                            }
                        }
                    }else if(fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".gif") ||
                            fileName.toLowerCase().endsWith(".bmp")){
                        String name = fileName.substring(0, fileName.lastIndexOf("."));
                        int type = 0;
                        if(name.toLowerCase().startsWith("case_")){
                            type=1;
                            name = name.toLowerCase().replace("case_","");
                        }else if(name.toLowerCase().startsWith("statement_")){
                            type=2;
                            name = name.toLowerCase().replace("statement_","");
                        }else if(name.toLowerCase().startsWith("branch_")){
                            type=3;
                            name = name.toLowerCase().replace("branch_","");
                        }
                        if(type==0){
                            continue;
                        }
                        PictureFileEntity pictureFileEntity = new PictureFileEntity();
                        pictureFileEntity.setFileName(fileName);
                        pictureFileEntity.setFileUrl(filelist[i].getAbsolutePath().replace(path, ""));
                        //pictureFileEntity.setFunctionName((baoName + "." + name.substring(name.lastIndexOf("_") + 1, name.length())).toLowerCase());
                        if(isSingle){
                            pictureFileEntity.setFunctionName(name.toLowerCase());
                        }else{
                            pictureFileEntity.setFunctionName(baoName.toLowerCase());
                        }
//                        pictureFileEntity.setProjectId(projectId);
//                        pictureFileEntity.setProjectVersion(version);
                        pictureFileEntity.setProjectId(0);
                        pictureFileEntity.setProjectVersion("");
                        pictureFileEntity.setFileType(type);
                        pictureFileEntity.setCreatPerson(user.getUsername());
                        pictureFileEntity.setCreatTime(now);
                        //List<PictureFileEntity> list = pictureFileDao.selectByNameAndProjectIdAndVersion(pictureFileEntity.getFunctionName(), type, projectId, version);
                        hashValue = findHashValue(pictureFileEntity.getFunctionName(), procedureDtoList);
                        if(hashValue != null && !hashValue.isEmpty()){
                            pictureFileEntity.setHashValue(hashValue);
                            List<PictureFileEntity> list = pictureFileDao.findPictureFileByFuncNameAndType(pictureFileEntity.getFunctionName(), type, hashValue);
                            if(list == null || list.size() == 0){
                                pictureFileDao.insert(pictureFileEntity);
                            }else{
                                pictureFileDao.updatePictureFileByFuncNameAndType(pictureFileEntity);
                                deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
                            }
                        }
                    }else if(fileName.endsWith(".docx")){
                        WordMubanEntity wordMubanEntity = new WordMubanEntity();
                        wordMubanEntity.setFileName(fileName);
                        wordMubanEntity.setFileUrl(filelist[i].getAbsolutePath().replace(path, ""));
                        wordMubanEntity.setProjectId(projectId);
                        wordMubanEntity.setProjectVersion(version);
                        wordMubanEntity.setCreatPerson(user.getUsername());
                        wordMubanEntity.setCreatTime(now);

                        if(fileName.startsWith("report")){
                            insetWordMuban(wordMubanEntity,projectId,version,1);
                        }else if(fileName.startsWith("description")){
                            insetWordMuban(wordMubanEntity,projectId,version,2);
                        }
                    }
                }
            }
        }
    }

    public  void scanUploadSingleFiles(File unFile,Integer project,String version,String fullName, String hashValue, Integer fileType, Integer pictureType, String path) throws IOException{
        User user = currentUserService.getUser();
        Date now = new Date();
        String fileName = unFile.getName();
        String absPath = unFile.getAbsolutePath();
        String longName = "";
        if(fullName.contains("(")){
            longName = fullName.substring(0,fullName.lastIndexOf("("));
        }else{
            longName = fullName;
        }
        if(procedureService.isOverloadedFun(project,version,longName)){
            longName = fullName.replace("(","_").replace(")","").replace(",","_");
        }

        if(fileType == 1){
            String name = fileName.substring(0, fileName.lastIndexOf(".")).toLowerCase();
            TcfFileEntity tcfFileEntity = new TcfFileEntity();
            tcfFileEntity.setFileName(fileName);
            tcfFileEntity.setFileUrl(absPath.replace(path,""));
            tcfFileEntity.setFunctionName(longName);
            tcfFileEntity.setProjectId(0);
            tcfFileEntity.setProjectVersion("");
            tcfFileEntity.setCreatPerson(user.getUsername());
            tcfFileEntity.setCreatTime(now);
            tcfFileEntity.setHashValue(hashValue);
            List<TcfFileEntity> list = tcfFileDao.findTcfFileByFuncName(tcfFileEntity.getFunctionName(), hashValue);
            if(list == null || list.size() == 0){
                tcfFileEntity.setNum("0");
                tcfFileDao.insert(tcfFileEntity);
            }else{
                tcfFileDao.updateTcfFileByFuncName(tcfFileEntity);
                deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
            }
        }else if(fileType == 2){
            Document doc = Jsoup.parse(unFile, "UTF-8");
            List<Map<String, Object>> result = findResultFromHtml(doc);
            String compar = longName.toLowerCase();
            int k=0;
            for(; k<result.size(); k++){
                if(compar.equals(result.get(k).get("name").toString().toLowerCase())){
                    insertCoverageSingle(fileName, unFile, path, user, now, longName, hashValue, result.get(k));
                    break;
                }
            }

            if(k == result.size()){
                findResultFromHtml2(doc, result);
                compar = compar.substring(compar.lastIndexOf(".")+1, compar.length());
                int m=0;
                for(; m<result.size(); m++){
                    if(compar.equals(result.get(m).get("longName").toString().toLowerCase())){
                        insertCoverageSingle(fileName, unFile, path, user, now, longName, hashValue, result.get(m));
                        break;
                    }
                }
                if(m == result.size()){
                    throw new BusinessException("未解析到关联函数覆盖率信息.");
                }
            }
        }else if(fileType == 3){
            PictureFileEntity pictureFileEntity = new PictureFileEntity();
            pictureFileEntity.setFileName(fileName);
            pictureFileEntity.setFileUrl(absPath.replace(path, ""));
            pictureFileEntity.setFunctionName(longName);
            pictureFileEntity.setProjectId(0);
            pictureFileEntity.setProjectVersion("");
            pictureFileEntity.setFileType(pictureType);
            pictureFileEntity.setCreatPerson(user.getUsername());
            pictureFileEntity.setCreatTime(now);
            pictureFileEntity.setHashValue(hashValue);
            List<PictureFileEntity> list = pictureFileDao.findPictureFileByFuncNameAndType(pictureFileEntity.getFunctionName(), pictureType, hashValue);
            if(list == null || list.size() == 0){
                pictureFileDao.insert(pictureFileEntity);
            }else{
                pictureFileDao.updatePictureFileByFuncNameAndType(pictureFileEntity);
                deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
            }
        }
    }

    public void insertCoverage(String fileName, File currentFile, String path, User user, Date now, String baoName, Map<String,Object> item, List<ProcedureDto> procedureDtoList, Boolean isSingle){
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        CoverageFileEntity coverageFileEntity = new CoverageFileEntity();
        coverageFileEntity.setFileName(fileName);
        coverageFileEntity.setFileUrl(currentFile.getAbsolutePath().replace(path, ""));
//                                    coverageFileEntity.setProjectId(projectId);
//                                    coverageFileEntity.setProjectVersion(version);
        coverageFileEntity.setProjectId(0);
        coverageFileEntity.setProjectVersion("");
        coverageFileEntity.setCreatPerson(user.getUsername());
        coverageFileEntity.setCreatTime(now);
        if(isSingle){
            coverageFileEntity.setFunctionName(name.toLowerCase());
        }else{
            coverageFileEntity.setFunctionName(baoName.toLowerCase());
        }

        coverageFileEntity.setStatementCoverage(item.get("statement").toString());
        coverageFileEntity.setBranchCoverage(item.get("branch").toString());
        if(item.containsKey("mcdc")){
            coverageFileEntity.setMcdcCoverage(item.get("mcdc").toString());
        }else{
            coverageFileEntity.setMcdcCoverage("");
        }
        String hashValue = findHashValue(coverageFileEntity.getFunctionName(), procedureDtoList);
        //List<CoverageFileEntity> list = coverageFileDao.selectByNameAndProjectIdAndVersion(coverageFileEntity.getFunctionName(), projectId, version);
        if(hashValue != null && !hashValue.isEmpty()){
            coverageFileEntity.setHashValue(hashValue);
            List<CoverageFileEntity> list = coverageFileDao.findCoverageFileByFuncName(coverageFileEntity.getFunctionName(), hashValue);
            if(list == null || list.size() == 0){
                coverageFileDao.insert(coverageFileEntity);
            }else{
                coverageFileDao.updateCoverageFileByFuncName(coverageFileEntity);
                deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
            }
        }
    }

    public void insertCoverageSingle(String fileName, File currentFile, String path, User user, Date now, String longName, String hashValue, Map<String,Object> item){
        CoverageFileEntity coverageFileEntity = new CoverageFileEntity();
        coverageFileEntity.setFileName(fileName);
        coverageFileEntity.setFileUrl(currentFile.getAbsolutePath().replace(path, ""));
        coverageFileEntity.setProjectId(0);
        coverageFileEntity.setProjectVersion("");
        coverageFileEntity.setCreatPerson(user.getUsername());
        coverageFileEntity.setCreatTime(now);
        coverageFileEntity.setFunctionName(longName);

        coverageFileEntity.setStatementCoverage(item.get("statement").toString());
        coverageFileEntity.setBranchCoverage(item.get("branch").toString());
        if(item.containsKey("mcdc")){
            coverageFileEntity.setMcdcCoverage(item.get("mcdc").toString());
        }else{
            coverageFileEntity.setMcdcCoverage("");
        }
        coverageFileEntity.setHashValue(hashValue);
        List<CoverageFileEntity> list = coverageFileDao.findCoverageFileByFuncName(coverageFileEntity.getFunctionName(), hashValue);
        if(list == null || list.size() == 0){
            coverageFileDao.insert(coverageFileEntity);
        }else{
            coverageFileDao.updateCoverageFileByFuncName(coverageFileEntity);
            deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
        }
    }

    public void insetWordMuban(WordMubanEntity wordMubanEntity, Integer projectId, String version, Integer fileType){
        List<WordMubanEntity> list = wordMubanDao.selectByProjectIdAndVersion(projectId, version, fileType);
        wordMubanEntity.setFileType(fileType);
        if(list == null || list.size() == 0){
            wordMubanDao.insert(wordMubanEntity);
        }else{
            wordMubanEntity.setId(list.get(0).getId());
            wordMubanDao.updateByPrimaryKey(wordMubanEntity);
            deleteFile(new File(uploadPath+list.get(0).getFileUrl()));
        }
    }

    public List<Map<String, Object>> findResultFromHtml(Document doc){
        List<Map<String, Object>> result = new ArrayList<>();
        Elements tr = doc.select("h4:has(a[name='procedure_table'])+br+br+br+center table table tr:has(td)");
        for(int i=0; i<tr.size(); i++){
            Elements td = tr.get(i).select("td");
            Map<String, Object> tableValue = new HashMap<>();
            int find = 1;
            for(int j=0; j<td.size(); j++) {

                Element element = td.get(j);
                String text = element.text().trim();
                if(text.contains("-")){
                    text = "";
                }
                if(text != null && !"".equals(text) && find == 1) {
                    tableValue.put("name", text);
                    String href = element.select("a").attr("href");
                    if(href != null && href.contains("#")){
                        href = href.replace("#", "");
                    }
                    tableValue.put("href", href);
                    find ++;
                } else if(text != null && !"".equals(text) && find == 2) {
                    if(text != null && text.toLowerCase().contains("no")){
                        text = "";
                    }
                    if(text.contains("+")){
                        text = text.replace("+","");
                    }
                    tableValue.put("statement", text);
                    String statementColor = element.attr("bgcolor");
                    tableValue.put("statementColor", statementColor);
                    find++;
                } else if(text != null && !"".equals(text) && find == 3) {
                    if(text != null && text.toLowerCase().contains("no")){
                        text = "";
                    }
                    if(text.contains("+")){
                        text = text.replace("+","");
                    }
                    tableValue.put("branch", text);
                    String branchColor = element.attr("bgcolor");
                    tableValue.put("branchColor", branchColor);
                    find++;
                }else if(text != null && !"".equals(text) && find == 4) {
                    if(text != null && text.toLowerCase().contains("no")){
                        text = "";
                    }
                    tableValue.put("mcdc", text);
                }
            }
            if(tableValue.containsKey("name") && !"".equals(tableValue.get("name").toString())){
                result.add(tableValue);
            }
        }
        return result;
    }

    public void findResultFromHtml2(Document doc, List<Map<String, Object>> res){
        for(int i=0; i<res.size(); i++){
            if(i==0){
                res.get(i).put("longName", "");
                continue;
            }
//            // #5FC05A红色，#E17B87绿色，去验证
//            if("#5FC05A".equals(res.get(i).get("statementColor")) || "#5FC05A".equals(res.get(i).get("branchColor")) ||
//                    "#E17B87".equals(res.get(i).get("statementColor")) || "#E17B87".equals(res.get(i).get("branchColor"))){

            // find long name
            Elements pre = doc.select("a[name='"+res.get(i).get("href")+"']+br+br+br+hr+br+br+center+br+center+br+br+h4+center table tbody tr td pre");
            for(int j=0; j<pre.size(); j++){
                Element element = pre.get(j);
                String text = element.text().trim();
                String longName = findLongName(text);
                res.get(i).put("longName", longName);
                break;
            }
            if(pre == null || pre.size()  == 0){
                res.get(i).put("longName", "");
            }

            // find value
            findValue(doc, res.get(i));

//            }else{
//                res.get(i).put("longName", "");
//                continue;
//            }
        }
    }
    public String findLongName(String text){
        int first = 0;
        String functionName = "";
        if(text.contains("procedure")){
            first = text.indexOf("procedure");
        }else if(text.contains("function")){
            first = text.indexOf("function");
        }
        int last = text.indexOf("is");
        int last2 = text.indexOf("return");
        if(first > 0 && last > first && (last < last2 || last2 < 0)){
            text = text.substring(first, last);
        }else if(first > 0 && last2 > first && (last > last2 || last < 0)){
            text = text.substring(first, last2);
        }
        text = text.replace("procedure", "").replace("function", "").trim();
        if(text.contains("(")){
            last = text.indexOf("(");
            functionName = text.substring(0,last).trim();
            text = text.replace(functionName, "").replace("(", "").replace(")", "");
            String[] params = text.split(";");
            for(int i=0; i<params.length; i++){
                if(params[i].contains(":")){
                    String param = params[i];
                    String[] paramChild = param.split(",");
                    params[i] = param.substring(param.indexOf(":")+1, param.length()).replace("in ", "").replace("out ", "").replace("inout ", "").trim();
                    for(String s: paramChild){
                        functionName +=  "_" + params[i];
                    }
                }
            }
            return functionName.toLowerCase();
        }
        return text.toLowerCase();
    }

    public String findText(String text){
        String result = "";
        if(text != null && !"".equals(text)){
            if(text.contains("=") && text.contains("%")){
                result = text.substring(text.lastIndexOf("=")+1, text.lastIndexOf("%")).trim();
            }
        }
        return result;
    }

    public void findValue(Document doc, Map<String, Object> item){
        Elements tr = doc.select("a[name='"+item.get("href")+"']+br+br+br+hr+br+br+center+br+center table tbody tr td table tbody tr");
        if(tr.size() > 0) {
            Elements td = tr.get(0).select("td");
            for(int j=0; j<td.size(); j++) {
                Element element = td.get(j);
                String text = element.text().trim();
                text = findText(text);
                if (j == 0) {
                    item.put("statement", text);
                } else if (j == 1) {
                    item.put("branch", text);
                } else if (j == 2) {
                    item.put("mcdc", text);
                }
            }
        }
    }

    public void deleteFile(File file){
//        File uploadDir = new File(uploadPath);
        if(file.exists() && file.isFile()){
//            File parent = file.getParentFile();
            file.delete();
 //           deleteFile(parent);
        }
//        else if(file.isDirectory()){
//            File[] files = file.listFiles();
//            if((files == null || files.length == 0) && file.getAbsolutePath() != uploadDir.getAbsolutePath()){
//                file.delete();
//            }
//        }
    }

    public void deleteDownloadFile(File file){
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 迭代删除文件夹
     * @param dirPath 文件夹路径
     */
    public void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if(files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    public List<TcfFileEntity> selectTcfByProjectIdAndVersion(int pageIndex, int pageSize, int projectId, String version){
        PageHelper.startPage(pageIndex, pageSize);
        List<TcfFileEntity> list = tcfFileDao.selectByProjectIdAndVersion(projectId, version);
//        List<TcfFileEntity> list = tcfFileDao.selectAll();
        return list;
    }

    public Integer selectMaxTcfByProjectIdAndVersion(int projectId, String version){
//        List<TcfFileEntity> list = tcfFileDao.selectByProjectIdAndVersion(projectId, version);
//        List<Integer> intList = new ArrayList<>();
//        for(TcfFileEntity tcf: list ){
//            String tmp = tcf.getNum();
//            if(tmp != null){
//                tmp = tmp.substring(tmp.lastIndexOf("_")+1);
//                tmp = tmp.substring(1, tmp.length());
//                intList.add(Integer.valueOf(tmp));
//            }
//        }
//        Integer max = 0;
//        if(intList.size() > 0){
//            max = Collections.max(intList);
//        }
//        return max;
        return 0;
    }

    public int deleteTcf(TcfFileEntity entity){
        int res = tcfFileDao.deleteByPrimaryKey(entity.getId());
        if(res > 0){
            deleteFile(new File(uploadPath+entity.getFileUrl()));
        }
        return res;
    }

    public List<CoverageFileEntity> selectCoverageByProjectIdAndVersion(int pageIndex, int pageSize, int projectId, String version){
        PageHelper.startPage(pageIndex, pageSize);
        List<CoverageFileEntity> list = coverageFileDao.selectByProjectIdAndVersion(projectId, version);
//        List<CoverageFileEntity> list = coverageFileDao.selectAll();
        return list;
    }

    public int deleteCoverage(CoverageFileEntity entity){
        int res = coverageFileDao.deleteByPrimaryKey(entity.getId());
        if(res > 0){
            deleteFile(new File(uploadPath+entity.getFileUrl()));
        }
        return res;
    }

    public List<PictureFileEntity> selectPictureByProjectIdAndVersion(int pageIndex, int pageSize, int projectId, String version){
        PageHelper.startPage(pageIndex, pageSize);
        List<PictureFileEntity> list = pictureFileDao.selectByProjectIdAndVersion(projectId, version);
//        List<PictureFileEntity> list = pictureFileDao.selectAll();
        return list;
    }

    public int deletePicture(PictureFileEntity entity){
        int res = pictureFileDao.deleteByPrimaryKey(entity.getId());
        if(res > 0){
            deleteFile(new File(uploadPath+entity.getFileUrl()));
        }
        return res;
    }

    public List<WordMubanEntity> selectWordMubanByProjectIdAndVersion(int projectId, String version, Integer fileType){
        List<WordMubanEntity> list = wordMubanDao.selectByProjectIdAndVersion(projectId, version, fileType);
        return list;
    }

    public String copyAllFiles(Integer project, String version) throws IOException{
        String rarPath = "";
        List<TcfFileEntity> tcfFileEntities = tcfFileDao.selectByProjectIdAndVersion(project, version);
        List<CoverageFileEntity> coverageFileEntities = coverageFileDao.selectByProjectIdAndVersion(project, version);
        List<PictureFileEntity> pictureFileEntities = pictureFileDao.selectByProjectIdAndVersion(project, version);
        if(tcfFileEntities.size() > 0 || coverageFileEntities.size() > 0 || pictureFileEntities.size() > 0){
            String uuid = UUID.randomUUID().toString();
            String downloadDir = downloadPath + uuid;
            File dir = new File(downloadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String path = dir.getAbsolutePath();
            for(TcfFileEntity tcf: tcfFileEntities){
                String allPath[] = tcf.getFileUrl().split(MyFileUtil.separator);
                copy(path, allPath, tcf.getFileUrl());
            }
            for(CoverageFileEntity coverage: coverageFileEntities){
                String allPath[] = coverage.getFileUrl().split(MyFileUtil.separator);
                copy(path, allPath, coverage.getFileUrl());
            }
            for(PictureFileEntity picture: pictureFileEntities){
                String allPath[] = picture.getFileUrl().split(MyFileUtil.separator);
                copy(path, allPath, picture.getFileUrl());
            }
            rarPath=path;
        }
        return rarPath;
    }

    public void copy(String path, String[] allPath, String url) throws IOException{
        try{
            if(allPath.length >= 2){
                allPath[1] = "";
                for(int i=0; i<allPath.length; i++){
                    if(!"".equals(allPath[i])){
                        path+=File.separator+allPath[i];
                    }
                }
                File copyTo = new File(path);
                if (!copyTo.exists()) {
                    if(!copyTo.getParentFile().exists()){
                        if(copyTo.getParentFile().mkdirs()){
                            if(copyTo.createNewFile()){
                                Files.copy(new File(uploadPath+File.separator+url).toPath(), copyTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    } else {
                        if(copyTo.createNewFile()){
                            Files.copy(new File(uploadPath+File.separator+url).toPath(), copyTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } else {
                    Files.copy(new File(uploadPath+File.separator+url).toPath(), copyTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                // FileCopyUtils.copy(new File(uploadPath+File.separator+url), copyTo);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
