package com.example.demo.word;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/16 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.config.Name;
import com.deepoove.poi.data.MiniTableRenderData;

public class DescData {

    @Name("function")
    private MiniTableRenderData function;
    @Name("description")
    private ListTextData description;
    @Name("testcase")
    private ListTextData testcase;
    @Name("input")
    private ListTextData input;
    @Name("output")
    private ListTextData output;
    @Name("statement")
    private ListTextData statement;
    @Name("branch")
    private ListTextData branch;

    public MiniTableRenderData getFunction() {
        return function;
    }

    public void setFunction(MiniTableRenderData function) {
        this.function = function;
    }

    public ListTextData getTestcase() {
        return testcase;
    }

    public void setTestcase(ListTextData testcase) {
        this.testcase = testcase;
    }

    public ListTextData getInput() {
        return input;
    }

    public void setInput(ListTextData input) {
        this.input = input;
    }

    public ListTextData getOutput() {
        return output;
    }

    public void setOutput(ListTextData output) {
        this.output = output;
    }

    public ListTextData getStatement() {
        return statement;
    }

    public void setStatement(ListTextData statement) {
        this.statement = statement;
    }

    public ListTextData getBranch() {
        return branch;
    }

    public void setBranch(ListTextData branch) {
        this.branch = branch;
    }

    public ListTextData getDescription() {
        return description;
    }

    public void setDescription(ListTextData description) {
        this.description = description;
    }
}
