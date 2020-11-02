package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/27 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

public class SegmentResultData {
    private String function;
    private String testcase;
    private String input;
    private String output;
    private String statement;
    private String branch;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getTestcase() {
        return testcase;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
