package com.example.demo.service;

import com.example.demo.dao.RolePermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    public void insertRolePermission(int roleId, List<Integer> permissionIdList, Timestamp createTime, Timestamp updateTime){
//        rolePermissionDao.insertRolePermission(roleId, permissionId, createTime, updateTime);
        for (int permissionId : permissionIdList) {
            rolePermissionDao.insertRolePermission(roleId, permissionId, createTime, updateTime);
        }
    }

    public int[] selectPermissionIds(int roleId){
        return rolePermissionDao.selectPermissionIds(roleId);
    }

    public void deleteRolePermission(int roleId){
        rolePermissionDao.deleteRolePermission(roleId);
    }

    public void delRolePermission(int roleId, List<Integer> permissionIdList){
        for (int permissionId : permissionIdList){
            rolePermissionDao.delRolePermission(roleId, permissionId);
        }
    }

    public void deletePermission(int permissionId){
        rolePermissionDao.deletePermission(permissionId);
    }
}
