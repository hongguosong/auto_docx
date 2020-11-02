package com.example.demo.dao;

import com.example.demo.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface RoleDao {
    List<RoleEntity> selectRoles();

    void insertRole(@Param("roleName")String roleName,
                    @Param("createTime")Timestamp createTime,
                    @Param("updateTime")Timestamp updateTime,
                    @Param("roleDesc")String roleDesc);

    void updateRole(@Param("id") int id,
                    @Param("roleName")String roleName,
                    @Param("createTime")Timestamp createTime,
                    @Param("updateTime")Timestamp updateTime,
                    @Param("roleDesc")String roleDesc);

    void deleteRole(@Param("id")int id);

    int selectRoleId(@Param("roleName")String roleName);

    String selectRoleNameById(@Param("id") int id);

    List<RoleEntity> selectRoleByRoleName(@Param("roleName")String roleName);
}
