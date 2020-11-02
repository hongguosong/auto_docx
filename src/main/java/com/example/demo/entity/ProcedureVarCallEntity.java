package com.example.demo.entity;

public class ProcedureVarCallEntity {
    private Integer id;

    private Integer procedureId;

    private Integer calledVarId;

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

    public Integer getCalledVarId() {
        return calledVarId;
    }

    public void setCalledVarId(Integer calledVarId) {
        this.calledVarId = calledVarId;
    }
}