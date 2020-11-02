package com.example.demo.entity;

public class FunDescEntity {
    private Integer id;

    private String name;

    private String comment;

    private String varDescription;

    private String inputDescription;

    private String outputDescription;

    private Integer procedureId;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getVarDescription() {
        return varDescription;
    }

    public void setVarDescription(String varDescription) {
        this.varDescription = varDescription == null ? null : varDescription.trim();
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription == null ? null : inputDescription.trim();
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription == null ? null : outputDescription.trim();
    }

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }
}