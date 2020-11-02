package com.example.demo.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class LogEntity {
    private Integer id;
    private String log;
    private String createPerson;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}
