package com.example.demo.controller;

import com.example.demo.service.RolePermissionService;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/RolePermission")
@RestController
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/insertRolePermission")
    public void insertRolePermission(@RequestParam("roleName")String roleName,
                                     @RequestParam("permissionIds[]")int[] permissionIds,    //接收数组类型时 @RequestParam("permissionIds[]") 需要加[]
                                     @RequestParam("createTime")String createTime,
                                     @RequestParam("updateTime")String updateTime){
        Timestamp create = Timestamp.valueOf(createTime);
        Timestamp update = Timestamp.valueOf(updateTime);
        int roleId = roleService.selectRoleId(roleName);
        List<Integer> insertPermissionIdList = Arrays.stream( permissionIds ).boxed().collect(Collectors.toList());
        rolePermissionService.insertRolePermission(roleId, insertPermissionIdList, create, update);
    }

    @RequestMapping("/selectPermissionIds")
    public int[] selectPermissionIds(@RequestParam("id")int roleId){
        return rolePermissionService.selectPermissionIds(roleId);
    }
}
