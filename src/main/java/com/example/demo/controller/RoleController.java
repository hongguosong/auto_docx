package com.example.demo.controller;

import com.example.demo.entity.PermissionEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RolePermissionService;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/selectRoles")
    public List<RoleEntity> selectRoles(){
        return roleService.selectRoles();
    }

    @RequestMapping("/insertRole")
    public void insertRole(@RequestParam("roleName")String roleName,
                           @RequestParam("createTime")String createTime,
                           @RequestParam("updateTime")String updateTime,
                           @RequestParam("roleDesc")String roleDesc){
        Timestamp create = Timestamp.valueOf(createTime);
        Timestamp update = Timestamp.valueOf(updateTime);
        roleService.insertRole(roleName, create, update, roleDesc);
    }

    @RequestMapping("/updateRole")
    public void updateRole(@RequestParam("id")int id,
                           @RequestParam("roleName")String roleName,
                           @RequestParam("createTime")String createTime,
                           @RequestParam("updateTime")String updateTime,
                           @RequestParam(value = "permissionIds[]", defaultValue = "")int[] permissionIds,    //接收数组类型时 @RequestParam("permissionIds[]") 需要加[]
                           @RequestParam("roleDesc")String roleDesc){
        Timestamp create = Timestamp.valueOf(createTime);
        Timestamp update = Timestamp.valueOf(updateTime);
        roleService.updateRole(id, roleName, create, update, roleDesc);
//        if (permissionIds.length > 0){
//            rolePermissionService.deleteRolePermissionTree(id);
//            rolePermissionService.insertRolePermission(id, permissionIds, create, update);
//        }
        if (permissionIds.length > 0){
            int[] prePermissionIds = permissionService.selectPrePermissionIds(id);
            //将int数组转为List<Integer>
            List<Integer> currentPermissionIdList = Arrays.stream( permissionIds ).boxed().collect(Collectors.toList());
            List<Integer> prePermissionIdList = Arrays.stream( prePermissionIds ).boxed().collect(Collectors.toList());
            //求差集
            List<Integer> addPermissionIdList = currentPermissionIdList.stream().filter(num -> !prePermissionIdList.contains(num))
                    .collect(Collectors.toList());
            List<Integer> delPermissionIdList = prePermissionIdList.stream().filter(num -> !currentPermissionIdList.contains(num))
                    .collect(Collectors.toList());
            if (addPermissionIdList.size() > 0){
                rolePermissionService.insertRolePermission(id, addPermissionIdList, create, update);
            }
            if (delPermissionIdList.size() > 0){
                rolePermissionService.delRolePermission(id, delPermissionIdList);
            }
        }
    }

    @RequestMapping("/deleteRole")
    public void deleteRole(@RequestParam("id")int id){
        roleService.deleteRole(id);
    }

    @RequestMapping("/selectRoleByRoleName")
    public List<RoleEntity> selectRoleByRoleName(@RequestParam("roleName")String roleName){
        return roleService.selectRoleByRoleName(roleName);
    }
}
