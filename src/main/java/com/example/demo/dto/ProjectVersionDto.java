package com.example.demo.dto;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/19 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.util.Encode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ProjectVersionDto {
    private Integer id;

    private String name;

    private String description;

    private String gitAddress;

    private String gitName;

    private String gitPassword;

    private String projectCreatePerson;

    private String projectCreateTime;

    private String version;

    private Date createTime;

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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGitAddress() {
        return gitAddress;
    }

    public void setGitAddress(String gitAddress) {
        this.gitAddress = gitAddress;
    }

    public String getGitName() {
        return gitName;
    }

    public void setGitName(String gitName) {
        this.gitName = gitName;
    }

    public String getGitPassword() {
        return Encode.decode(this.gitPassword);
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public String getProjectCreatePerson() {
        return projectCreatePerson;
    }

    public void setProjectCreatePerson(String projectCreatePerson) {
        this.projectCreatePerson = projectCreatePerson;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public String getProjectCreateTime() {
        return projectCreateTime;
    }

    public void setProjectCreateTime(String projectCreateTime) {
        this.projectCreateTime = projectCreateTime;
    }
}
