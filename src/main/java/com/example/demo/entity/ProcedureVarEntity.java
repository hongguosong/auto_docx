package com.example.demo.entity;

public class ProcedureVarEntity {
    private Integer id;

    private Integer procedureId;

    private String name;

    private String cls;

    private String val;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls == null ? null : cls.trim();
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val == null ? null : val.trim();
    }
}