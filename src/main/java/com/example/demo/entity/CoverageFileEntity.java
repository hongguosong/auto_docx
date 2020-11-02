package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CoverageFileEntity {
    private Integer id;

    private String fileName;

    private String fileUrl;

    private String functionName;

    private String statementCoverage;

    private String branchCoverage;

    private String mcdcCoverage;

    private String creatPerson;

    private Date creatTime;

    private Integer projectId;

    private String projectVersion;

    private String hashValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName == null ? null : functionName.trim();
    }

    public String getStatementCoverage() {
        return statementCoverage;
    }

    public void setStatementCoverage(String statementCoverage) {
        this.statementCoverage = statementCoverage == null ? null : statementCoverage.trim();
    }

    public String getBranchCoverage() {
        return branchCoverage;
    }

    public void setBranchCoverage(String branchCoverage) {
        this.branchCoverage = branchCoverage == null ? null : branchCoverage.trim();
    }

    public String getMcdcCoverage() {
        return mcdcCoverage;
    }

    public void setMcdcCoverage(String mcdcCoverage) {
        this.mcdcCoverage = mcdcCoverage;
    }

    public String getCreatPerson() {
        return creatPerson;
    }

    public void setCreatPerson(String creatPerson) {
        this.creatPerson = creatPerson == null ? null : creatPerson.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion == null ? null : projectVersion.trim();
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
}