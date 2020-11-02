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
import com.deepoove.poi.data.PictureRenderData;

public class SegmentPictureData {

    private String function;
    @Name("testcase_pic")
    private PictureRenderData testcasePic;
    @Name("statement_pic")
    private PictureRenderData statementPic;
    @Name("branch_pic")
    private PictureRenderData branchPic;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public PictureRenderData getTestcasePic() {
        return testcasePic;
    }

    public void setTestcasePic(PictureRenderData testcasePic) {
        this.testcasePic = testcasePic;
    }

    public PictureRenderData getStatementPic() {
        return statementPic;
    }

    public void setStatementPic(PictureRenderData statementPic) {
        this.statementPic = statementPic;
    }

    public PictureRenderData getBranchPic() {
        return branchPic;
    }

    public void setBranchPic(PictureRenderData branchPic) {
        this.branchPic = branchPic;
    }
}
