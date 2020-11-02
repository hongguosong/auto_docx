package com.example.demo.security.entity;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.security.entity </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sys_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="nick_name")
    private String nickName;

    @Column(name="role_id")
    private String roleId;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="email")
    private String email;

    @Column(name="logout_id")
    private int logoutId;

    @Column(name="delete_id")
    private int deleteId;

    @Column(name="avatar")
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLogoutId() {
        return logoutId;
    }

    public void setLogoutId(int logoutId) {
        this.logoutId = logoutId;
    }

    public int getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(int deleteId) {
        this.deleteId = deleteId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
