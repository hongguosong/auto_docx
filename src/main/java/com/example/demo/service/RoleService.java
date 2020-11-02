package com.example.demo.service;

import com.example.demo.dao.RoleDao;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RolePermissionService rolePermissionService;

    public List<RoleEntity> selectRoles(){
        return roleDao.selectRoles();
    }

    public void insertRole(String roleName, Timestamp createTime, Timestamp updateTime, String roleDesc){
        roleDao.insertRole(roleName, createTime, updateTime, roleDesc);
    }

    public void updateRole(int id, String roleName, Timestamp createTime, Timestamp updateTime, String roleDesc){
        roleDao.updateRole(id, roleName, createTime, updateTime, roleDesc);
    }

    public void deleteRole(int id){
        //删除用户栏中的该角色
        String roleId = String.valueOf(id);
        List<UserEntity> entities = userService.selectUserByRoleId(id);
        for (UserEntity entity : entities){
            //Arrays.asList 将字符串转换成List时，转换后的是Arrays的ArrayList,并不是List的ArrayList
            List<String> roleIdList = new ArrayList<String>(Arrays.asList(entity.getRoleId().split(",")));
            roleIdList.removeIf(s -> s.equals(roleId));    //roleIdList删除roleId
            String roleIdStr = String.join(",", roleIdList);
            userService.updateRoleId(entity.getId(), roleIdStr);
        }
        //删除该角色对应的权限
        rolePermissionService.deleteRolePermission(id);
        // 删除该角色
        roleDao.deleteRole(id);
    }

    public int selectRoleId(String roleName){
        return roleDao.selectRoleId(roleName);
    }

    public String selectRoleNameById(int id){
        return roleDao.selectRoleNameById(id);
    }

    public List<RoleEntity> selectRoleByRoleName(String roleName){
        return roleDao.selectRoleByRoleName(roleName);
    }

    public String getRoleNameStr(String roleIdStr){
        if (roleIdStr.equals("")){
            return "";
        }
        String[] roleIdArr = roleIdStr.split(",");
        List<String> roleNameList = new ArrayList<>();
        for (String s : roleIdArr) {
            String roleName = roleDao.selectRoleNameById(Integer.parseInt(s));
            roleNameList.add(roleName);
        }
        return String.join(",", roleNameList);
    }
}
