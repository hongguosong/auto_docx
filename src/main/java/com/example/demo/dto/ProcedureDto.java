package com.example.demo.dto;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/20 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.entity.ProcedureEntity;

import java.util.Date;

public class ProcedureDto extends ProcedureEntity{
    private Integer insertFlag;

    private String createPerson;

    private Integer calledParentId;

    private String descComment;

    public Integer getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(Integer insertFlag) {
        this.insertFlag = insertFlag;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Integer getCalledParentId() {
        return calledParentId;
    }

    public void setCalledParentId(Integer calledParentId) {
        this.calledParentId = calledParentId;
    }

    public String getDescComment() {
        return descComment;
    }

    public void setDescComment(String descComment) {
        this.descComment = descComment;
    }
}
