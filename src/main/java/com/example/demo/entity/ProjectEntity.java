package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ProjectEntity {
    private Integer id;

    private String name;

    private String description;

    private String gitAddress;

    private String gitName;

    private String gitPassword;

    private String createPerson;

    private Date createTime;

    private Integer deleteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getGitAddress() {
        return gitAddress;
    }

    public void setGitAddress(String gitAddress) {
        this.gitAddress = gitAddress == null ? null : gitAddress.trim();
    }

    public String getGitName() {
        return gitName;
    }

    public void setGitName(String gitName) {
        this.gitName = gitName == null ? null : gitName.trim();
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword == null ? null : gitPassword.trim();
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }
}