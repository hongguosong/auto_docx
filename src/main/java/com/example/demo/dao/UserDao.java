package com.example.demo.dao;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface UserDao {
    UserDto selectCurrentUserInfo(@Param("username")String username);

//    List<UserEntity> selectUsers(@Param("startNum")int startNum, @Param("pageSize")int pageSize, @Param("orgId")int orgId);

    List<UserDto> selectUsers2(@Param("orgId")int orgId);

    int selectTotalCount();

    void insertUser(@Param("username")String username,
                    @Param("nickName")String nickName,
                    @Param("password")String password,
                    @Param("email")String email,
                    @Param("createTime")Timestamp createTime,
                    @Param("updateTime")Timestamp updateTime,
                    @Param("roleId")String roleId,
                    @Param("orgId")int orgId,
                    @Param("logoutId")int logoutId);

    void updateUser(@Param("id")int id,
                    @Param("username")String username,
                    @Param("nickName")String nickName,
                    @Param("email")String email,
                    @Param("createTime")Timestamp createTime,
                    @Param("updateTime")Timestamp updateTime,
                    @Param("roleId")String roleId,
                    @Param("logoutId")int logoutId);

    void deleteUser(@Param("id")int id);

    List<UserEntity> selectUserByOrgId(@Param("orgId")int orgId);

    List<UserEntity> selectUserByRoleId(@Param("roleId") int roleId);

    void updateRoleId(@Param("id")int id, @Param("roleId")String roleId);

    String selectRoleIdById(@Param("id")int id);

    List<UserEntity> selectUserByUsername(@Param("username")String username);

    void userSettings(@Param("avatar")String avatar,
                      @Param("username")String username,
                      @Param("email")String email,
                      @Param("password")String password);

    String selectAvatar(@Param("id")int id);
}
