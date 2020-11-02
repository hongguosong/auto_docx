package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.LogEntity;
import com.example.demo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/*
* 通过本地log文件夹路径递归生成无限树
* */
@RequestMapping("/log")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

//    @RequestMapping("/getLogList")
//    public JSONObject getLogList(@RequestParam(value = "pageIndex", defaultValue = "0", required = false)Integer pageIndex,
//                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false)Integer pageSize,
//                                 @RequestParam("log")String log,
//                                 @RequestParam("createPerson")String createPerson,
//                                 @RequestParam("createTime")String createTime){
//        JSONObject object = new JSONObject();
//        List<LogEntity> logEntities = logService.getLogList(pageIndex, pageSize, log, createPerson, createTime);
//        int totalCount = logService.getLogListCount(log, createPerson, createTime);
//        object.put("data", logEntities);
//        object.put("itemsCount", totalCount);
//        return object;
//    }

    @RequestMapping("/getLogList")
    public JSONObject getLogList(@RequestParam(value = "pageIndex", defaultValue = "0", required = false)Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false)Integer pageSize,
                                 @RequestParam("preDate")String preDate,
                                 @RequestParam("nextDate")String nextDate,
                                 @RequestParam("createPerson")String createPerson){
        JSONObject object = new JSONObject();
        List<LogEntity> logEntities = logService.getLogList(pageIndex, pageSize, preDate, nextDate, createPerson);
        int totalCount = logService.getLogListCount(preDate, nextDate, createPerson);
        object.put("data", logEntities);
        object.put("itemsCount", totalCount);
        return object;
    }

    @RequestMapping("/getCreatePersonList")
    public List<LogEntity> getCreatePersonList(){
        return logService.getCreatePersonList();
    }

}
