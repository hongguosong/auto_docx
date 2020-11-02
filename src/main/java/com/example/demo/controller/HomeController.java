package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.*;
import com.example.demo.security.entity.User;
import com.example.demo.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String goHome(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("username", user.getUsername());
        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "home";
    }

    @RequestMapping("/pages/function")
    public String goFunction(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        HomeController homeController = new HomeController();
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        homeController.getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));

        return "pages/function";
    }

    @RequestMapping("/pages/user")
    public String goUser(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        paramMap.put("userId", user.getId());

        return "pages/user";
    }

    @RequestMapping("/pages/group")
    public String goGroup(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/organization";
    }

    @RequestMapping("/pages/log")
    public String goLog(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/log";
    }

    @RequestMapping("/pages/permission")
    public String goPermission(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/permission";
    }

    @RequestMapping("/pages/system")
    public String goSystem(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/system";
    }

    @RequestMapping("/pages/project")
    public String goProject(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/project";
    }

    @RequestMapping("/pages/promblem")
    public String goPromblem(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/promblem";
    }

    @RequestMapping("/pages/tcfFile")
    public String goTcfFile(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/tcf_file";
    }

    @RequestMapping("/pages/coverageFile")
    public String goCoverageFile(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/coverage_file";
    }

    @RequestMapping("/pages/wordTempFile")
    public String goWordTempFile(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/wordTemp_file";
    }

    @RequestMapping("/pages/picture")
    public String goPicture(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/picture";
    }

    @RequestMapping("/pages/testCase")
    public String goTestCase(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/testCase";
    }

    @RequestMapping("/pages/role")
    public String goRole(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/role";
    }

    @RequestMapping("/pages/userSetting")
    public String goUserSetting(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        paramMap.put("username", user.getUsername());
        paramMap.put("email", user.getEmail());
        return "pages/user_setting";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String goLogin() {
        return "pages/login";
    }

    @RequestMapping("/projectVersion")
    public String goProjectVersion(Map<String, Object> paramMap,
                                   @RequestParam(name = "projectId") Integer projectId,
                                   @RequestParam(name = "projectName") String projectName) {
        if(getUser().getId() == 0){
            return "/pages/login";
        }
        List<ProjectVersionEntity> entityList= projectService.selectByProjectId(projectId);
        Map<String, Object> project = new HashMap<String, Object>();
        project.put("id", projectId);
        project.put("name", projectName);
        List<Map<String, Object>> versionList = new ArrayList<>();
        for(ProjectVersionEntity entity: entityList){
            Map<String, Object> version = new HashMap<>();
            version.put("id", entity.getId());
            version.put("name", entity.getProjectVersion());
            String commitString = entity.getCommits();
            JSONArray jsonArray = JSONArray.parseArray(commitString);
            List<Map<String, Object>> commitsList = new ArrayList<>();
            Map<String, Object> commits = new HashMap<>();
            for(int i=0; i<jsonArray.size(); i++){
                commits.put("message", jsonArray.getJSONObject(i).getString("message"));
                commits.put("timestamp", jsonArray.getJSONObject(i).getString("timestamp"));
                commits.put("author", jsonArray.getJSONObject(i).getJSONObject("author").getString("name"));
                commitsList.add(commits);
            }
            version.put("commitsList", commitsList);
            versionList.add(version);
        }
        project.put("versionList", versionList);

//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -10);
//        Map<String, Object> project = new HashMap<String, Object>();
//        List versionList = new ArrayList();
//        project.put("name", projectName);
//        for(int j=0;j<3;j++){
//            Map<String, Object> version = new HashMap<String, Object>();
//            version.put("name", "version-1000"+String.valueOf(j+1));
//            version.put("desc", "modify some functions...");
//            version.put("user", "user "+String.valueOf(j+1));
//            version.put("date", calendar.getTime());
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//            versionList.add(0, version);
//        }
//        project.put("versionList", versionList);

        paramMap.put("project", project);
        return "projectVersion";
    }

    @ResponseBody
    @RequestMapping("/projectList")
    public List getProjectList(Map<String, Object> paramMap) {
        Random random = new Random();
        List projectList = new ArrayList();
        for(int i=0;i<3;i++){
            Map<String, Object> project = new HashMap<String, Object>();
            project.put("name", "Project-00"+String.valueOf(i+1));
            project.put("desc", "project description xxx "+String.valueOf(i+1));
            project.put("version", "v1.0."+String.valueOf(random.nextInt(9000)+1000));
            project.put("status", random.nextInt(3));
            projectList.add(project);
        }

        return projectList;
    }

    @RequestMapping("/version")
    public String goVersion(Map<String, Object> paramMap,
                            @RequestParam(name = "project") Integer project,
                            @RequestParam(name = "version") String version,
                            @RequestParam(name = "projectName")String projectName) {
        Random random = new Random();
        List functionList = new ArrayList();
        int functionNum = random.nextInt(5) + 2;
        for(int i=0;i<functionNum;i++){
            Map<String, Object> function = new HashMap<String, Object>();
            List callList = new ArrayList();
            List beCalledList = new ArrayList();
            List varList = new ArrayList();
            List problemList = new ArrayList();
            function.put("name", "function"+String.valueOf(i+1));
            function.put("desc", "function description xxx"+String.valueOf(i+1));
            int callNum = random.nextInt(3) + 2;
            for(int j=0;j<callNum;j++){
                Map<String, Object> callFunction = new HashMap<String, Object>();
                callFunction.put("name", "function"+String.valueOf(random.nextInt(9)+1)+String.valueOf(random.nextInt(9)+1));
                callList.add(callFunction);
            }
            int beCalledNum = random.nextInt(3) + 2;
            for(int j=0;j<beCalledNum;j++){
                Map<String, Object> callFunction = new HashMap<String, Object>();
                callFunction.put("name", "function"+String.valueOf(random.nextInt(9)+1)+String.valueOf(random.nextInt(9)+1));
                beCalledList.add(callFunction);
            }
            int varNum = random.nextInt(5) + 4;
            for(int j=0;j<varNum;j++){
                Map<String, Object> variable = new HashMap<String, Object>();
                variable.put("name", "variable"+String.valueOf(random.nextInt(9)+1)+String.valueOf(random.nextInt(9)+1));
                varList.add(variable);
            }
            int problemNum = random.nextInt(5) + 4;
            for(int j=0;j<problemNum;j++){
                Map<String, Object> problem = new HashMap<String, Object>();
                problem.put("name", "problem "+String.valueOf(random.nextInt(9)+1)+String.valueOf(random.nextInt(9)+1));
                problemList.add(problem);
            }

            function.put("callList", callList);
            function.put("beCalledList", beCalledList);
            function.put("varList", varList);
            function.put("problemList", problemList);
            functionList.add(function);
        }

        paramMap.put("functionList", functionList);
        paramMap.put("project", project);
        paramMap.put("version", version);
        paramMap.put("projectName", projectName);
        return "version";
    }

    @ResponseBody
    @RequestMapping("/proVerProblemList")
    public List getProjectVerFunList(@RequestParam(name = "project") Integer project,
                                     @RequestParam(name = "version") String version) {
        Random random = new Random();
        List projectVerFunList = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(int i=0;i<23;i++){
            Map<String, Object> func = new HashMap<String, Object>();
            func.put("name", "Problem-00"+String.valueOf(i+1));
            func.put("desc", "Problem description xxx "+String.valueOf(i+1));
            func.put("submit", "person: "+String.valueOf(random.nextInt(9000)+1000));
            func.put("createTime", sdf.format(new Date()));
            projectVerFunList.add(func);
        }

        return projectVerFunList;
    }

    @ResponseBody
    @RequestMapping("/proVerReportList")
    public List getProVerReportList(@RequestParam(name = "project") Integer project,
                                     @RequestParam(name = "version") String version) {
        Random random = new Random();
        List projectVerFunList = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(int i=0;i<23;i++){
            Map<String, Object> func = new HashMap<String, Object>();
            func.put("name", "Problem-00"+String.valueOf(i+1));
            func.put("desc", "Problem description xxx "+String.valueOf(i+1));
            func.put("submit", "person: "+String.valueOf(random.nextInt(9000)+1000));
            func.put("createTime", sdf.format(new Date()));
            projectVerFunList.add(func);
        }

        return projectVerFunList;
    }

    @ResponseBody
    @PostMapping("/proVerTcfList")
    public Map<String, Object> getProVerTcfList(@RequestBody JSONObject jsonObject,
                                 @RequestParam(name = "project") Integer project,
                                 @RequestParam(name = "version") String version) {
        PageInfo<TcfFileEntity> pageInfo =new PageInfo<>(fileUploadService.selectTcfByProjectIdAndVersion(jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize"), project, version));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @ResponseBody
    @PostMapping("/deleteProVerTcfList")
    public String deleteProVerTcfList(@RequestBody List<TcfFileEntity> list) {
        for(int i=0; i<list.size(); i++){
            fileUploadService.deleteTcf(list.get(i));
        }
        return JsonResult.success();
    }

    @ResponseBody
    @PostMapping("/proVerWordmubanList")
    public List<WordMubanEntity> getProVerWordMubanList(@RequestParam(name = "project") Integer project,
                                                        @RequestParam(name = "version") String version,
                                                        @RequestParam(name = "fileType") Integer fileType) {
        return fileUploadService.selectWordMubanByProjectIdAndVersion(project, version, fileType);
    }

    @ResponseBody
    @PostMapping("/proVerCoverageList")
    public Map<String, Object> getProVerCoverageList(@RequestBody JSONObject jsonObject,
                                      @RequestParam(name = "project") Integer project,
                                      @RequestParam(name = "version") String version) {
        PageInfo<CoverageFileEntity> pageInfo =new PageInfo<>(fileUploadService.selectCoverageByProjectIdAndVersion(jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize"), project, version));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @ResponseBody
    @PostMapping("/deleteProVerCoverageList")
    public String deleteProVerCoverageList(@RequestBody List<CoverageFileEntity> list) {
        for(int i=0; i<list.size(); i++){
            fileUploadService.deleteCoverage(list.get(i));
        }
        return JsonResult.success();
    }

    @ResponseBody
    @PostMapping("/proVerPictureList")
    public Map<String, Object> getProVerPictureList(@RequestBody JSONObject jsonObject,
                                     @RequestParam(name = "project") Integer project,
                                     @RequestParam(name = "version") String version) {
        PageInfo<PictureFileEntity> pageInfo =new PageInfo<>(fileUploadService.selectPictureByProjectIdAndVersion(jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize"), project, version));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @ResponseBody
    @PostMapping("/deleteProVerPictureList")
    public String deleteProVerPictureList(@RequestBody List<PictureFileEntity> list) {
        for(int i=0; i<list.size(); i++){
            fileUploadService.deletePicture(list.get(i));
        }
        return JsonResult.success();
    }

    public User getUser() { //为了从session获取用户信息,可以配置如下
        User user = new User();
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) user = (User) auth.getPrincipal();
        return user;
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @RequestMapping("/pages/email")
    public String goEmail(Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/test";
    }

    @RequestMapping("/pages/wizard")
    public String goWizard (Map<String, Object> paramMap) {
        User user = getUser();
        if(user.getId() == 0){
            return "/pages/login";
        }
        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/project-wizard";
    }

    //根据登录用户权限生成菜单
    public void getMenu(Map<String, Object> paramMap, List<PermissionEntity> permissionEntities){
        //对permissionEntities关于 id 进行排序
        permissionEntities.sort((o1, o2) -> {
            if (o1.getId() < o2.getId()) {
                return -1;
            } else if (o1.getId() > o2.getId()) {
                return 1;
            }
            return 0;
        });

        //生成菜单
        List menuList = new ArrayList();

        if (permissionEntities.size() > 0){
            List<PermissionEntity> parentList = new ArrayList<>();
            List<PermissionEntity> remainList1 = new ArrayList<>();
            for (int i = 0; i < permissionEntities.size(); i++){
                if (permissionEntities.get(i).getParentId() == 0){
                    parentList.add(permissionEntities.get(i));
                } else {
                    remainList1.add(permissionEntities.get(i));
                }
            }

            //一级菜单
            for (int j = 0; j < parentList.size(); j++){
                Map<String, Object> menu1 = new HashMap<String, Object>();
                List list1 = new ArrayList();
                List<PermissionEntity> childrenList1 = new ArrayList<>();
                List<PermissionEntity> remainList2 = new ArrayList<>();
                menu1.put("menuName", parentList.get(j).getMenuName());
                menu1.put("url", parentList.get(j).getUrl());
                for (int k = 0; k < remainList1.size(); k++){
                    if (remainList1.get(k).getParentId() == parentList.get(j).getId()){
                        childrenList1.add(remainList1.get(k));
                    } else {
                        remainList2.add(remainList1.get(k));
                    }
                }
                if (childrenList1.size() > 0){
                    menu1.put("hasChildren", true);
                    menu1.put("children", list1);
                }else{
                    menu1.put("hasChildren", false);
                }
                menuList.add(menu1);

                //二级菜单
                for (int p = 0; p < childrenList1.size(); p++){
                    Map<String, Object> menu2 = new HashMap<String, Object>();
                    List list2 = new ArrayList();
                    List<PermissionEntity> childrenList2 = new ArrayList<>();
                    menu2.put("menuName", childrenList1.get(p).getMenuName());
                    menu2.put("url", childrenList1.get(p).getUrl());

                    //三级菜单
                    for (int q = 0; q < remainList2.size(); q++){
                        if (remainList2.get(q).getParentId() == childrenList1.get(p).getId()){
                            childrenList2.add(remainList2.get(q));
                            Map<String, Object> menu3 = new HashMap<String, Object>();
                            menu3.put("menuName", remainList2.get(q).getMenuName());
                            menu3.put("url", remainList2.get(q).getUrl());
                            menu3.put("hasChildren", false);
                            list2.add(menu3);
                        }
                    }
                    if (childrenList2.size() > 0){
                        menu2.put("hasChildren", true);
                        menu2.put("children", list2);
                    } else {
                        menu2.put("hasChildren", false);
                    }
                    list1.add(menu2);
                }
            }
        }
        paramMap.put("menuList", menuList);
    }

    @ResponseBody
    @RequestMapping("/selectFunctionsByProjectId")
    public List<ProcedureDto> selectFunctionsByProjectId(@RequestParam("projectId")int projectId,
                                                         @RequestParam("projectVersion")String projectVersion){
        return procedureService.selectFunctionsByProjectId(projectId, projectVersion);
    }

    @ResponseBody
    @RequestMapping("/getFuncByProIdPagination")
    public List<ProcedureDto> getFuncByProIdPagination(@RequestParam("projectId")int projectId,
                                                       @RequestParam(value = "pageIndex")int pageIndex,
                                                       @RequestParam(value = "pageSize")int pageSize){
        int startNum = (pageIndex - 1) * pageSize;
        return procedureService.getFuncByProIdPagination(projectId, startNum, pageSize);
    }

    @ResponseBody
    @RequestMapping("/getFuncCallByProAndV")
    public List<ProcedureDto> getFuncCallByProAndV(@RequestParam("projectId")int projectId,
                                                   @RequestParam("projectVersion")String projectVersion){
        return procedureService.getFuncCallByProAndV(projectId, projectVersion);
    }

}
