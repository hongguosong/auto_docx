package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/8 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONObject;
import com.example.demo.analyzer.AdaAnalyzer;
import com.example.demo.analyzer.AnalyzerDatabase;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.ProjectVersionEntity;
import com.example.demo.service.ProcedureService;
import com.example.demo.service.ProjectService;
import com.example.demo.util.Encode;
import com.example.demo.util.GitUtils;
import com.example.demo.util.MyStringUtils;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@RestController
public class GitLabController {
    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //定义本地git路径
    @Value("${gitCodePath}")
    public String LOCALPATH;
    @Value("${pretreatmentPath}")
    private String PRETREATMENT_PATH;
    //.git文件路径
    public String LOCALGITFILE = LOCALPATH + ".git";
    //远程仓库地址
    public String REMOTEREPOURI = "https://git.lug.ustc.edu.cn/hongguosong/test_webhook.git";
    //操作git的用户名
    public String USER = "";
    //密码
    public String PASSWORD = "";
    //git远程仓库服务器ip
    public String HOST = "172.16.124.101";
    //建立与远程仓库的联系，仅需要执行一次

    /**
     * 测试
     * 1,下载ngrok，运行 ngrok http 8084
     暴露出外网地址，Forwarding    http://41cc0005.ngrok.io -> http://localhost:8084
     这样外网地址就可以访问本地的服务了.

     2,github配置webhook，找到项目seting->webhooks,
     Payload URL:
     http://41cc0005.ngrok.io/payload
     Content type:
     application/json
     Which events would you like to trigger this webhook?
     选择Just the push event.

     3,gitlab配置webhook，找到项目setting->Integrations,
     URL:
     http://41cc0005.ngrok.io/payload
     Trigger,选择
     Push events和Merge request events

     4,git配置 http.sslverify=false
     终端输入命令： git config --global http.sslVerify false
     查看命令: git config -l
     * @param requestJson
     * @return
     */
    @PostMapping("/payload")
    public String receiveFromGitLab(@RequestBody JSONObject requestJson) {
        simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.success("检测到后台webhooks"));
        String resString = "";
        LOGGER.error(requestJson.toJSONString());
        //只接受push钩子
        String kind = requestJson.getString("object_kind");
        if(kind == null || !kind.equals("tag_push")){
            resString = "不是tag钩子";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,resString));
            return resString;
        }
        // 获取git仓库
        JSONObject repository = requestJson.getJSONObject("repository");
        if(repository == null){
            resString = "未解析到repository";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,resString));
            return resString;
        }
        // 获取工程名称
        String name = repository.getString("name");
        // github
        //String cloneUrl = repository.getString("git_ssh_url");
        // gitlab
        String cloneUrl = repository.getString("git_http_url");
        // clone width ssh与clone width https区别 这里用https方式获取
        //https用443端口，可以对repo根据权限进行读写，只要有账号密码就可进行操作。
        //ssh则用的是22端口，也可以对repo根据权限进行读写，但是需要SSH Keys授权，这个key是通过ssh key生成器生成的，然后放在github上，作为授权的证据，这样的话就不需要用户名和密码进行授权了。
        String ref = requestJson.getString("ref");
        String version =  ref.substring(ref.lastIndexOf("/")+1);
        String userName = requestJson.getString("user_name");
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String commits = ("[{\"author\":{\"name\":\""+userName+"\"},\"timestamp\":\""+now+"\"}]");
        //String commits = requestJson.getJSONArray("commits").toJSONString();
        if(StringUtil.isEmpty(name) || StringUtil.isEmpty(cloneUrl) || StringUtil.isEmpty(version) || StringUtil.isEmpty(commits)){
            resString = "name或cloneUrl或version或commits为空";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,resString));
            return resString;
        }

        if(!version.startsWith("v")){
            resString = "测试钩子,正式版本钩子请以小写v开头";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,resString));
            return resString;
        }

        // 数据库查询相应项目，用对应的用户名和密码填写USER和PASSWORD
        ProjectEntity projectEntity = projectService.selectByGitAddress(cloneUrl);
        if(projectEntity == null){
            resString = "未添加对应git地址的项目";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,cloneUrl+"\n"+resString));
            return resString;
        }

        Path gitLocal = Paths.get(LOCALPATH+name+"//.git");
//        try{
//            String command = "git config --global --unset user.name";
//            Runtime.getRuntime().exec(command);
//            command = "git config --global --unset user.email";
//            Runtime.getRuntime().exec(command);
//        }catch(Exception e){
//            resString = e.getMessage();
//            LOGGER.error(resString);
//            return resString;
//        }
        String password = Encode.decode(projectEntity.getGitPassword());
        if(password.isEmpty()){
            resString = "密码解码失败";
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,"项目"+projectEntity.getName()+resString));
            LOGGER.error(resString);
            return resString;
        }
        //password = "ZhJcxFLncCvYhxdn3c_n";
        projectEntity.setGitPassword(Encode.decode(projectEntity.getGitPassword()));
        if(!Files.isReadable(gitLocal)){
            resString = GitUtils.setupRepo(cloneUrl, projectEntity.getGitName(), password, LOCALPATH+name);
        }else{
            resString = GitUtils.pullBranchToLocal(LOCALPATH+name+"//.git", projectEntity.getGitName(), password);
        }
        if(!resString.isEmpty()){
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20, "项目"+projectEntity.getName()+"\n"+resString));
            return resString;
        }

        //执行脚本
//        File parent = new File(LOCALPATH+name);
        File shFile = new File(LOCALPATH+name+File.separator+"init.sh");
        if(shFile.exists()){

            try{
                ProcessBuilder builder = new ProcessBuilder("/bin/chmod","777", shFile.getAbsolutePath());
                Process process = builder.start();
                int rc = process.waitFor();

            }catch (IOException e){
                resString = e.getMessage();
                LOGGER.error(resString);
                simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20, "项目"+projectEntity.getName()+"\n"+resString));
                return resString;
            }catch (InterruptedException e){
                resString = e.getMessage();
                LOGGER.error(resString);
                simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20, "项目"+projectEntity.getName()+"\n"+resString));
                return resString;
            }

            ProcessBuilder pb = new ProcessBuilder(shFile.getAbsolutePath());
            int runningStatus = 0;
            String s = null;
            try {
                Process p = pb.start();
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((s = stdInput.readLine()) != null) {
                    LOGGER.error(s);
                }
                while ((s = stdError.readLine()) != null) {
                    LOGGER.error(s);
                }
                try {
                    runningStatus = p.waitFor();
                } catch (InterruptedException e) {
                    resString = e.getMessage();
                    LOGGER.error(resString);
                    return resString;
                }
            }catch (Exception e){
                resString = e.getMessage();
                LOGGER.error(resString);
                simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20, "项目"+projectEntity.getName()+"\n"+resString));
                return resString;
            }

//            try{
//                ProcessBuilder builder = new ProcessBuilder("git", "checkout", ".");
//                Process process = builder.start();
//                int rc = process.waitFor();
//
//            }catch (IOException e){
//                resString = e.getMessage();
//                LOGGER.error(resString);
//                return resString;
//            }catch (InterruptedException e){
//                resString = e.getMessage();
//                LOGGER.error(resString);
//                return resString;
//            }
        }

        // TODO 关联projectId和version
        Integer projectId = projectEntity.getId();

        ProjectVersionEntity projectVersionEntity = new ProjectVersionEntity();
        projectVersionEntity.setProjectId(projectId);
        projectVersionEntity.setProjectVersion(version);
        projectVersionEntity.setCommits(commits);
        projectVersionEntity.setCreateTime(new Date());
        int addPVResult = projectService.addProjectVersion(projectVersionEntity);
        if(addPVResult == 10){
            resString = "版本号重复";
            LOGGER.error(resString);
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,"项目"+projectEntity.getName()+resString));
            return resString;
        }

        try{
            AdaAnalyzer.PRETREATMENT_DIR = PRETREATMENT_PATH;
            AnalyzerDatabase db = AdaAnalyzer.analyzeAdaSource(LOCALPATH+name+"//");
            procedureService.updateProcedureDatabase(projectId, version, db);
        }catch(Exception e){
            resString = e.getMessage();
            LOGGER.error(resString);
            resString = "项目"+projectEntity.getName()+"\n"+resString;
            if(resString != null && resString.contains("uk_sys_project_procedure")){
                Integer id = MyStringUtils.getId(resString);
                if(id != -1) {
                    ProcedureEntity procedureEntity = procedureService.findProcedureById(id);
                    resString = "项目" + projectEntity.getName() + "\n" + "有重复函数" + procedureEntity.getFullName();
                }
            }
            simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.failed(20,resString));
            return resString;
        }

        simpMessagingTemplate.convertAndSend("/topic/notification", JsonResult.success("项目"+projectEntity.getName()+"创建版本号"+version+"成功"));
        return "Success";
    }
}
