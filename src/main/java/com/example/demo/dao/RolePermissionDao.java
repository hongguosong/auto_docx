package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface RolePermissionDao {

    void insertRolePermission(@Param("roleId")int roleId,
                              @Param("permissionId") int permissionId,
                              @Param("createTime") Timestamp createTime,
                              @Param("updateTime") Timestamp updateTime);

    int[] selectPermissionIds(@Param("roleId")int roleId);

    void deleteRolePermission(@Param("roleId")int roleId);

    void delRolePermission(@Param("roleId")int roleId, @Param("permissionId") int permissionId);

    void deletePermission(@Param("permissionId")int permissionId);
}
