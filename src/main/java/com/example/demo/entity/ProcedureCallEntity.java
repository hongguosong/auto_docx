package com.example.demo.entity;

public class ProcedureCallEntity {
    private Integer id;

    private Integer procedureId;

    private Integer calledId;

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

    public Integer getCalledId() {
        return calledId;
    }

    public void setCalledId(Integer calledId) {
        this.calledId = calledId;
    }
}