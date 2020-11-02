package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.ProcedureDao;
import com.example.demo.dao.ProjectVersionDao;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class FunctionCompareService {
    @Autowired
    private ProcedureDao procedureDao;
    @Autowired
    private ProjectVersionDao projectVersionDao;

    public JSONObject selectFunctionsByProjectId2(int pageIndex, int pageSize, int projectId){
        int startNum = pageIndex * pageSize;
        JSONObject object = new JSONObject();
        String projectVersion = null;
        List<ProcedureDto> procedureDtos = new ArrayList<>();
        int itemsCount = 0;
        if (projectId != 0) {
            //获取当前该项目的最新版本
            List<ProjectVersionEntity> projectVersionEntities = projectVersionDao.selectByProjectId(projectId);
            projectVersionEntities = projectVersionEntities.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(ProjectVersionEntity::getProjectId))), ArrayList::new));
            if(projectVersionEntities.size() > 0){
                projectVersion = projectVersionEntities.get(0).getProjectVersion();
                procedureDtos = procedureDao.selectFunctionsByProjectId2(startNum, pageSize, projectId, projectVersion);
                itemsCount = procedureDao.selectFunctionsByProjectId(projectId, projectVersion).size();
            }
        }
        object.put("data", procedureDtos);
        object.put("itemsCount", itemsCount);
        return object;
    }

    public List<ProcedureEntity> selectByProjectId(int projectId){
        return procedureDao.findProjectUse(projectId);
    }
}
