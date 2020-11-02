package com.example.demo.entity;

public class ProblemEntity {
    private Integer id;

    private String funcName;

    private String testCaseId;

    private Integer errorType;

    private Integer errorLevel;

    private String testEnvironment;

    private String testCaseName;

    private String testInput;

    private String problemDescription;

    private String suggest;

    private String solution;

    private String closedCycleResult;

    private String testPerson;

    private String testTime;

    private String remark;

    private String other;

    private Integer projectId;

    private String projectVersion;

    private Integer procedureId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName == null ? null : funcName.trim();
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId == null ? null : testCaseId.trim();
    }

    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public Integer getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(Integer errorLevel) {
        this.errorLevel = errorLevel;
    }

    public String getTestEnvironment() {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment) {
        this.testEnvironment = testEnvironment == null ? null : testEnvironment.trim();
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName == null ? null : testCaseName.trim();
    }

    public String getTestInput() {
        return testInput;
    }

    public void setTestInput(String testInput) {
        this.testInput = testInput == null ? null : testInput.trim();
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription == null ? null : problemDescription.trim();
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution == null ? null : solution.trim();
    }

    public String getClosedCycleResult() {
        return closedCycleResult;
    }

    public void setClosedCycleResult(String closedCycleResult) {
        this.closedCycleResult = closedCycleResult == null ? null : closedCycleResult.trim();
    }

    public String getTestPerson() {
        return testPerson;
    }

    public void setTestPerson(String testPerson) {
        this.testPerson = testPerson == null ? null : testPerson.trim();
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime == null ? null : testTime.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
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
}