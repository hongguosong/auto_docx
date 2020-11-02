package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.ProblemPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Liu
 * @create 2019-11-25
 */
@Controller
@RequestMapping(value = "problemProperty")
public class ProblemPropertyController {
    @Autowired
    private ProblemPropertyService problemPropertyService;

    /**
     * 获取问题单列表
     * @param pageIndex 当前页
     * @param pageSize 页大小
     * @return 问题单列表
     */
    @RequestMapping(value = "getList")
    @ResponseBody
    public JSONObject getList (@RequestParam(value = "pageIndex",defaultValue = "0",required = false) Integer pageIndex,
                               @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize) {

        return problemPropertyService.getList(pageIndex,pageSize);
    }

    /**
     * 获取所有问题单动态属性
     * @return 属性列表
     */
    @RequestMapping(value = "getAll")
    @ResponseBody
    public JSONArray getAll () {
        return problemPropertyService.getAll();
    }

    /**
     * 新增记录
     * @param item 记录
     * @return 新增记录
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public int addProblemProperty (@RequestParam(value = "item") String item) {
        return problemPropertyService.addProblemProperty(item);
    }

    /**
     * 更新记录
     * @param item 记录
     * @return 更新记录
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public int updateProblemProperty (@RequestParam(value = "item") String item) {
        return problemPropertyService.updateProblemProperty(item);
    }

    /**
     * 按ID删除记录
     * @param id 记录ID
     * @return 删除标识
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public int deleteProblemProperty (@RequestParam(value = "id") Integer id) {
        return problemPropertyService.deleteProblemProperty(id);
    }
}
