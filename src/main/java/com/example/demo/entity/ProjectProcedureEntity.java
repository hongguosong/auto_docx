package com.example.demo.entity;

public class ProjectProcedureEntity {
    private Integer id;

    private Integer projectId;

    private String projectVersion;

    private Integer procedureId;

    private Integer insertFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public Integer getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(Integer insertFlag) {
        this.insertFlag = insertFlag;
    }
}