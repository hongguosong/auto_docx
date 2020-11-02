package com.example.demo.service;

import com.example.demo.dao.LogDao;
import com.example.demo.entity.LogEntity;
import com.example.demo.entity.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class LogService {
    @Autowired
    private LogDao logDao;

//    public List<LogEntity> getLogList(int pageIndex, int pageSize, String log, String createPerson, String createTime){
//        int startNum = pageIndex * pageSize;
//        return logDao.getLogList(startNum, pageSize, log, createPerson, createTime);
//    }

    public List<LogEntity> getLogList(int pageIndex, int pageSize, String preDate, String nextDate, String createPerson){
        int startNum = pageIndex * pageSize;
        Timestamp preTime = null;
        Timestamp nextTime = null;
        if (!preDate.equals("")){
            preDate = preDate + " 00:00:00";
            preTime = Timestamp.valueOf(preDate);
        }
        if (!nextDate.equals("")){
            nextDate = nextDate + " 24:00:00";
            nextTime = Timestamp.valueOf(nextDate);
        }
        return logDao.getLogList(startNum, pageSize, preTime, nextTime, createPerson);
    }

    public int getLogListCount(String preDate, String nextDate, String createPerson){
        Timestamp preTime = null;
        Timestamp nextTime = null;
        if (!preDate.equals("")){
            preDate = preDate + " 00:00:00";
            preTime = Timestamp.valueOf(preDate);
        }
        if (!nextDate.equals("")){
            nextDate = nextDate + " 24:00:00";
            nextTime = Timestamp.valueOf(nextDate);
        }
        return logDao.getLogListCount(preTime, nextTime, createPerson);
    }

    public void insertLog(String log, String createPerson, Timestamp createTime){
        logDao.insertLog(log, createPerson, createTime);
    }

    public List<LogEntity> getCreatePersonList(){
        List<LogEntity> logEntities = logDao.getCreatePersonList();
        logEntities = logEntities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(LogEntity:: getCreatePerson))), ArrayList::new));
        return logEntities;
    }
}
