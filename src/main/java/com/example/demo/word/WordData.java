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

import com.deepoove.poi.config.Name;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;

public class WordData {

    @Name("tcf_function")
    private MiniTableRenderData tcfFunction;
    @Name("static_function")
    private MiniTableRenderData staticFunction;
//    @Name("segment_result")
//    private DocxRenderData segmentResult;
//    @Name("segment_picture")
//    private DocxRenderData segmentPicture;
    @Name("segment_problem")
    private DocxRenderData segmentProblem;
//    @Name("function_name")
//    private ListTextData functionName;
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
//    @Name("testcase_pic")
//    private ListPictureData testcasePic;
//    @Name("statement_pic")
//    private ListPictureData statementPic;
//    @Name("branch_pic")
//    private ListPictureData branchPic;


    public MiniTableRenderData getTcfFunction() {
        return tcfFunction;
    }

    public void setTcfFunction(MiniTableRenderData tcfFunction) {
        this.tcfFunction = tcfFunction;
    }

    public MiniTableRenderData getStaticFunction() {
        return staticFunction;
    }

    public void setStaticFunction(MiniTableRenderData staticFunction) {
        this.staticFunction = staticFunction;
    }

    public DocxRenderData getSegmentProblem() {
        return segmentProblem;
    }

    public void setSegmentProblem(DocxRenderData segmentProblem) {
        this.segmentProblem = segmentProblem;
    }

    public ListTextData getTestcase() {
        return testcase;
    }

    public void setTestcase(ListTextData testcase) {
        this.testcase = testcase;
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
}

