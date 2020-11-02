package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.LogDao;
import com.example.demo.dao.ProblemDao;
import com.example.demo.entity.ProblemEntity;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Liu
 * @create 2019-12-02
 */
@Service
public class ProblemService {
    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private UserService userService;

    public List<ProblemEntity> selectAll() {
        return problemDao.selectAll();
    }

    public List<ProblemEntity> getList (Integer project,String version,JSONObject jsonObject) {
        PageHelper.startPage(jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize"));
        JSONObject object = new JSONObject();
        List<ProblemEntity> list = problemDao.selectByProjectAndVersion(project, version);
        return list;
    }

    public List<ProblemEntity> getByProcedureId(Integer procedureId) {
        return problemDao.getByProcedureId(procedureId);
    }

    public List<ProblemEntity> getByProcedureIdAndProjectAndVersion(Integer procedureId, Integer project, String version) {
        return problemDao.getByProcedureIdAndProjectAndVersion(procedureId, project, version);
    }

    public ProblemEntity selectByPrimaryKey(Integer id) {
        return problemDao.selectByPrimaryKey(id);
    }

    public int deleteProblemById(Integer id, String functionName, String projectName, String projectVersion) {
        String log = "删除版本为“" + projectVersion + "”的“" + projectName + "”项目的“" + functionName + "”函数的问题单";
        String createPerson = currentUserService.getUser().getUsername();
        Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDao.insertLog(log, createPerson, createTime);

        return problemDao.deleteByPrimaryKey(id);
    }

    public int insert(ProblemEntity record, String projectName, String projectVersion) {
        // ProblemEntity record = JSONObject.parseObject(data,ProblemEntity.class);

        String log = "新增版本为“" + projectVersion + "”的“" + projectName + "”项目的“" + record.getFuncName() + "”函数的问题单";
        String createPerson = currentUserService.getUser().getUsername();
        Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDao.insertLog(log, createPerson, createTime);

        return problemDao.insert(record);
    }

    public int updateByPrimaryKey(ProblemEntity record, String projectName, String projectVersion) {
        // ProblemEntity record = JSONObject.parseObject(data,ProblemEntity.class);

        if (record.getId() == null) {
            return 0;
        }

        String log = "修改版本为“" + projectVersion + "”的“" + projectName + "”项目的“" + record.getFuncName() + "”函数的问题单";
        String createPerson = currentUserService.getUser().getUsername();
        Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDao.insertLog(log, createPerson, createTime);

        return problemDao.updateByPrimaryKey(record);
    }
}
