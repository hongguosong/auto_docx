package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/28 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.config.Name;
import com.deepoove.poi.data.TextRenderData;

public class SegmentProblemData {

    @Name("problem_property")
    private ProblemPropertyData problemProperty;

    @Name("problem_num")
    private String problemNum;

    private String function;

    @Name("testcase_id")
    private String testcaseId;

    private TextRenderData error1;

    private TextRenderData error2;

    private TextRenderData error3;

    private TextRenderData error4;

    private TextRenderData level1;

    private TextRenderData level2;

    private TextRenderData level3;

    @Name("test_environment")
    private String testEnvironment;

    @Name("testcase_name")
    private String testCaseName;

    @Name("test_input")
    private String testInput;

    @Name("problem_description")
    private String problemDescription;

    private String suggest;

    private String solution;

    @Name("closed_cycle_result")
    private String closedCycleResult;

    @Name("test_perosn")
    private String testPerson;

    @Name("test_time")
    private String testTime;

    private String remark;

    public ProblemPropertyData getProblemProperty() {
        return problemProperty;
    }

    public void setProblemProperty(ProblemPropertyData problemProperty) {
        this.problemProperty = problemProperty;
    }

    public String getProblemNum() {
        return problemNum;
    }

    public void setProblemNum(String problemNum) {
        this.problemNum = problemNum;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(String testcaseId) {
        this.testcaseId = testcaseId;
    }

    public TextRenderData getError1() {
        return error1;
    }

    public void setError1(TextRenderData error1) {
        this.error1 = error1;
    }

    public TextRenderData getError2() {
        return error2;
    }

    public void setError2(TextRenderData error2) {
        this.error2 = error2;
    }

    public TextRenderData getError3() {
        return error3;
    }

    public void setError3(TextRenderData error3) {
        this.error3 = error3;
    }

    public TextRenderData getError4() {
        return error4;
    }

    public void setError4(TextRenderData error4) {
        this.error4 = error4;
    }

    public TextRenderData getLevel1() {
        return level1;
    }

    public void setLevel1(TextRenderData level1) {
        this.level1 = level1;
    }

    public TextRenderData getLevel2() {
        return level2;
    }

    public void setLevel2(TextRenderData level2) {
        this.level2 = level2;
    }

    public TextRenderData getLevel3() {
        return level3;
    }

    public void setLevel3(TextRenderData level3) {
        this.level3 = level3;
    }

    public String getTestEnvironment() {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getTestInput() {
        return testInput;
    }

    public void setTestInput(String testInput) {
        this.testInput = testInput;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getClosedCycleResult() {
        return closedCycleResult;
    }

    public void setClosedCycleResult(String closedCycleResult) {
        this.closedCycleResult = closedCycleResult;
    }

    public String getTestPerson() {
        return testPerson;
    }

    public void setTestPerson(String testPerson) {
        this.testPerson = testPerson;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
