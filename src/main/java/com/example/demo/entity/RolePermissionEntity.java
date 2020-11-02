package com.example.demo.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RolePermissionEntity {
    private Integer id;
    private Integer roleId;
    private Integer permissionId;
    private String createTime;
    private String updateTime;
    private String deleteStatus;
    private Integer deleteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
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
        this.updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }
}
