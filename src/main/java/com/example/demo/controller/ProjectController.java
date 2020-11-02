package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/18 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */


import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.dto.ProjectVersionDto;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.ProjectVersionEntity;
import com.example.demo.service.LogService;
import com.example.demo.service.ProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.bouncycastle.math.ec.ScaleYPointMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    @PostMapping("/add")
    public String addProject(@RequestBody ProjectEntity projectEntity){
        projectService.addProject(projectEntity);
        return JsonResult.success();
    }
    @PostMapping("/update")
    public String updateProject(@RequestBody ProjectEntity projectEntity){
        projectService.updateProject(projectEntity);
        return JsonResult.success();
    }
    @PostMapping("/selectProWidthLastVer")
    public Map<String, Object> getAllProject(@RequestBody JSONObject jsonObject) {
        PageInfo<ProjectVersionDto> pageInfo =new PageInfo<>(projectService.selectProWidthLastVer(jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize")));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @RequestMapping("/deleteProject")
    public String deleteProject(@RequestBody JSONObject jsonObject){
        int id = jsonObject.getInteger("id");
        projectService.deleteProject(id);

        return JsonResult.success();
    }

    @RequestMapping("/deleteProjectVersion")
    public String deleteProjectVersion(@RequestParam(name = "project") Integer project,
                                       @RequestParam(name = "version") String version) {
        projectService.deleteProjectVersion(project, version);
        return JsonResult.success();
    }
}
