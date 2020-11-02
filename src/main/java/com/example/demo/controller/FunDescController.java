package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/9 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.config.exception.JsonResult;
import com.example.demo.entity.FunDescEntity;
import com.example.demo.service.CurrentUserService;
import com.example.demo.service.FunDescService;
import com.example.demo.service.LogService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("fundesc")
public class FunDescController {
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private FunDescService funDescService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @PostMapping("/selectByProcedureId")
    public String selectByProcedureId(@RequestParam(value = "procedureId") Integer procedureId){
        Map<String,Object> list = new HashMap<>();
        list.put("list",funDescService.selectByProcedureId(procedureId));
        return JsonResult.success(list);
    }

    @PostMapping("/selectByFullName")
    public String selectByFullName(@RequestParam(value = "fullName") String fullName){
        Map<String,Object> list = new HashMap<>();
        list.put("list",funDescService.selectByFullName(fullName));
        return JsonResult.success(list);
    }

    @PostMapping("/add")
    public String add(@RequestBody FunDescEntity entity){
        String log = "更新了函数";
        String createPerson = currentUserService.getUser().getUsername();
        Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logService.insertLog(log, createPerson, createTime);

        funDescService.add(entity);
        return JsonResult.success();
    }
}
