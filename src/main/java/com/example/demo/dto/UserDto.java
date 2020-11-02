package com.example.demo.dto;

import com.example.demo.entity.UserEntity;

public class UserDto extends UserEntity {
    private String roleName;
    private String logoutName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLogoutName() {
        return logoutName;
    }

    public void setLogoutName(String logoutName) {
        this.logoutName = logoutName;
    }
}
