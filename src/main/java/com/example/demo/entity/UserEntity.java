package com.example.demo.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class UserEntity {
    private Integer id;
    private String username;
    private String password;
    private String nickName;
    private String roleId;
    private String createTime;
    private String updateTime;
    private String email;
    private Integer orgId;
    private Integer logoutId;
    private Integer deleteId;
    private String avatar;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
    }

    public Integer getLogoutId() {
        return logoutId;
    }

    public void setLogoutId(Integer logoutId) {
        this.logoutId = logoutId;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
