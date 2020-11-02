package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.entity.ProblemEntity;
import com.example.demo.service.ProblemService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liu
 * @create 2019-12-02
 */
@Controller
@RequestMapping(value = "problem")
public class ProblemController {
    @Autowired
    public ProblemService problemService;

    /**
     * 查询所有问题单
     * @return 所有问题单
     */
    @RequestMapping(value = "getAll")
    @ResponseBody
    public List<ProblemEntity> selectAll() {
        return problemService.selectAll();
    }

    /**
     * 获取问题单列表
     * @return 问题单列表
     */
    @RequestMapping(value = "getList")
    @ResponseBody
    public Map<String, Object> getList (@RequestBody JSONObject jsonObject,
                                        @RequestParam(name = "project") Integer project,
                                        @RequestParam(name = "version") String version) {

        PageInfo<ProblemEntity> pageInfo = new PageInfo<>(problemService.getList(project, version, jsonObject));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    /**
     * 根据函数ID查询问题单
     * @param procedureId 问题单ID
     * @return 函数关联的问题单
     */
    @RequestMapping(value = "getByProcedureId")
    @ResponseBody
    public String getByProcedureId(@RequestParam("procedureId") Integer procedureId) {
        Map<String,Object> list = new HashMap<>();
        list.put("list", problemService.getByProcedureId(procedureId));
        return JsonResult.success(list);
    }

    /**
     * 根据函数ID查询问题单
     * @param procedureId 问题单ID
     * @return 函数关联的问题单
     */
    @RequestMapping(value = "getByProcedureIdAndProjectAndVersion")
    @ResponseBody
    public String getByProcedureIdAndProjectAndVersion(@RequestParam("procedureId") Integer procedureId,
                                                @RequestParam("project") Integer project,
                                                @RequestParam("version") String version) {
        Map<String,Object> list = new HashMap<>();
        list.put("list", problemService.getByProcedureIdAndProjectAndVersion(procedureId, project, version));
        return JsonResult.success(list);
    }

    /**
     * 根据问题单ID查询
     * @param id 问题单ID
     * @return 该ID的问题单
     */
    @RequestMapping(value = "getByProblemId")
    @ResponseBody
    public String selectByPrimaryKey(@RequestParam("problemId") Integer id) {
        Map<String,Object> pbm = new HashMap<>();
        pbm.put("pbm", problemService.selectByPrimaryKey(id));
        return JsonResult.success(pbm);
    }

    /**
     * 删除问题单
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteProblemById")
    @ResponseBody
    public String deleteProblemById(@RequestParam("problemId") Integer id,
                                    @RequestParam("functionName")String functionName,
                                    @RequestParam("projectName")String projectName,
                                    @RequestParam("projectVersion")String projectVersion) {
        problemService.deleteProblemById(id, functionName, projectName, projectVersion);
        return JsonResult.success();
    }

    /**
     * 新增问题单
     * @param data 问题单数据
     * @return 标识
     */
    @RequestMapping(value = "addProblem")
    @ResponseBody
    public String insert(@RequestBody ProblemEntity data,
                      @RequestParam("projectName")String projectName,
                      @RequestParam("projectVersion")String projectVersion) {
        problemService.insert(data, projectName, projectVersion);
        return JsonResult.success();
    }

    /**
     * 更新问题单
     * @param data 问题单数据
     * @return 标识
     */
    @RequestMapping(value = "updateProblem")
    @ResponseBody
    public String updateByPrimaryKey(@RequestBody ProblemEntity data,
                                  @RequestParam("projectName")String projectName,
                                  @RequestParam("projectVersion")String projectVersion) {
        problemService.updateByPrimaryKey(data, projectName, projectVersion);
        return JsonResult.success();
    }
}
