package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.BusinessException;
import com.example.demo.dao.ProblemPropertyDao;
import com.example.demo.entity.ProblemPropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liu
 * @create 2019-11-25
 */
@Service
public class ProblemPropertyService {
    @Autowired
    private ProblemPropertyDao problemPropertyDao;

    public JSONObject getList (Integer pageIndex,Integer pageSize) {
        JSONObject object = new JSONObject();
        List<ProblemPropertyEntity> list = problemPropertyDao.selectAll();
        Integer start = pageIndex * pageSize;
        List<ProblemPropertyEntity> list1 = problemPropertyDao.selectByPage(start,pageSize);
        object.put("data",list1);
        object.put("itemsCount",list.size());
        return object;
    }

    public JSONArray getAll () {
        List<ProblemPropertyEntity> list = problemPropertyDao.selectAll();
        JSONArray array = new JSONArray();
        for (ProblemPropertyEntity entity: list) {
            JSONObject object = new JSONObject();
            object.put("name",entity.getName());
            object.put("label",entity.getLabel());
            object.put("value","");
            array.add(object);
        }
        return array;
    }

    public int addProblemProperty (String item) {
        ProblemPropertyEntity record = JSONObject.parseObject(item, ProblemPropertyEntity.class);
        record = objectToTrim(record);

        int flag = 0;
        if (checkIsEmpty(record)) {
            return flag;
        }

        flag = problemPropertyDao.insert(record);
        System.out.println("add: "+flag+" record: "+record.getId());
        return flag;
    }

    public int updateProblemProperty (String item) {
        ProblemPropertyEntity record = JSONObject.parseObject(item, ProblemPropertyEntity.class);
        record = objectToTrim(record);

        int flag = 0;
        if (checkIsEmpty(record)) {
            return flag;
        }

        flag = problemPropertyDao.updateByPrimaryKey(record);
        System.out.println("update: "+flag+" record: "+record.getId());
        return flag;
    }

    public int deleteProblemProperty (Integer id) {
        int flag = problemPropertyDao.deleteByPrimaryKey(id);

        System.out.println("delete: "+flag+" record: "+id);
        return flag;
    }

    /**
     * ProblemPropertyEntity对象属性去空格
     * @param record ProblemPropertyEntity对象
     * @return 属性去空格的dProblemPropertyEntity对象
     */
    public ProblemPropertyEntity objectToTrim (ProblemPropertyEntity record) {
        record.setName(record.getName().trim());
        record.setLabel(record.getLabel().trim());
        record.setPropertyName(record.getPropertyName().trim());
        return record;
    }

    /**
     * 检查ProblemPropertyEntity对象是否有属性为空
     * @param record ProblemPropertyEntity对象
     * @return boolean值
     */
    public boolean checkIsEmpty (ProblemPropertyEntity record) {
        boolean check = false;
        if (record.getName().isEmpty() || record.getLabel().isEmpty() ||
                record.getPropertyName().isEmpty()) {
            check = true;
        }
        if(!record.getName().isEmpty()){
            if(!isEnglish(record.getName())){
                throw new BusinessException("字段名称只能填写英文字符.");
            }
            if(record.getName().equals("funcName") ||
                record.getName().equals("testCaseId") ||
                    record.getName().equals("errorType") ||
                    record.getName().equals("errorLevel") ||
                    record.getName().equals("testEnvironment") ||
                    record.getName().equals("testCaseName") ||
                    record.getName().equals("testInput") ||
                    record.getName().equals("problemDescription") ||
                    record.getName().equals("suggest") ||
                    record.getName().equals("solution") ||
                    record.getName().equals("closedCycleResult") ||
                    record.getName().equals("testPerson") ||
                    record.getName().equals("testTime") ||
                    record.getName().equals("remark") ||
                    record.getName().equals("other") ||
                    record.getName().equals("id") ||
                    record.getName().equals("projectId") ||
                    record.getName().equals("projectVersion") ||
                    record.getName().equals("procedureId")){
                throw new BusinessException("字段名称与已经存在的字段名称重复.");
            }
        }
        return check;
    }

    public boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }
}
