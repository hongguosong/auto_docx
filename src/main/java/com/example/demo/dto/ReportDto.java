package com.example.demo.dto;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/1/13 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.entity.ReportEntity;

public class ReportDto extends ReportEntity{
    private String emailLink;
    private String projectName;
    private String to;

    public String getEmailLink() {
        return emailLink;
    }

    public void setEmailLink(String emailLink) {
        this.emailLink = emailLink;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
