package com.example.demo.dto;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/21 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.entity.ProcedureCallEntity;

public class ProcedureCallDto extends ProcedureCallEntity{
    private String name;
    private String fullName;
    private String longName;
    private Integer version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
