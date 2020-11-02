package com.example.demo.dao;

import com.example.demo.entity.PermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionDao {

    List<PermissionEntity> selectAllPermissions();

    List<PermissionEntity> selectPermissions(@Param("startNum")int startNum,
                                             @Param("pageSize")int pageSize,
                                             @Param("menuName")String menuName,
                                             @Param("url")String url,
                                             @Param("menuCode")String menuCode,
                                             @Param("parentId")int parentId);

    int selectTotalCount(@Param("menuName")String menuName, @Param("url")String url,@Param("menuCode")String menuCode, @Param("parentId")int parentId);

    void insertPermission(@Param("menuName")String menuName, @Param("url")String url,@Param("menuCode")String menuCode, @Param("parentId")int parentId);

    void updatePermission(@Param("id")int id, @Param("menuName")String menuName, @Param("url")String url,@Param("menuCode")String menuCode, @Param("parentId")int parentId);

    void deletePermission(@Param("id")int id);

    List<PermissionEntity> selectPermissionByParentId(@Param("parentId")int parentId);

    List<PermissionEntity> selectPermissionByRoleId(@Param("roleId")int roleId);

    int[] selectPrePermissionIds(@Param("roleId")int roleId);
}
