package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.ProjectVersionEntity;
import com.example.demo.service.FunctionCompareService;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/functionCompare")
public class FunctionCompareController {
    @Autowired
    private FunctionCompareService functionCompareService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/selectProjects")
    public List<ProjectEntity> selectProjects(){
        return projectService.getAllProject();
    }

    @RequestMapping("/selectFunctionsByProjectId2")
    public JSONObject selectFunctionsByProjectId2(@RequestParam("pageIndex")int pageIndex,
                                                  @RequestParam("pageSize")int pageSize,
                                                  @RequestParam("projectId")int projectId){
        return functionCompareService.selectFunctionsByProjectId2(pageIndex, pageSize, projectId);
    }

    @RequestMapping("/selectProjectsByFunctionId")
    public JSONObject selectProjectsByFunctionId(@RequestParam("pageIndex")int pageIndex,
                                                 @RequestParam("pageSize")int pageSize,
                                                 @RequestParam("functionId")int functionId){

        return projectService.selectProjectsByFunctionId(pageIndex, pageSize, functionId);
    }

    @RequestMapping("/selectVByFuncIdAndProId")
    public List<Map<String, Object>> selectVByFuncIdAndProId(@RequestParam("functionId")int functionId,
                                                             @RequestParam("projectId")int projectId){
        return projectService.selectVByFuncIdAndProId(functionId, projectId);
    }

    @PostMapping("/compare")
    public JSONObject compare(@RequestParam("project1")int project1,
                              @RequestParam("project2")int project2){
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject objectCommon = new JSONObject();
        if(project1 == project2){
            List<ProcedureEntity> listCommon = functionCompareService.selectByProjectId(project1);
            object1.put("data", new ArrayList<>());
            object1.put("itemsCount", 0);
            object2.put("data", new ArrayList<>());
            object2.put("itemsCount", 0);
            objectCommon.put("data", listCommon);
            objectCommon.put("itemsCount", listCommon.size());
        }else{
            List<ProcedureEntity> list1 = functionCompareService.selectByProjectId(project1);
            List<ProcedureEntity> list2 = functionCompareService.selectByProjectId(project2);
            if(list1 != null && list1.size() > 0 && list2 != null && list2.size() >0){
                List<ProcedureEntity> newList1 = new ArrayList<>();
                List<ProcedureEntity> newList2 = new ArrayList<>();
                List<ProcedureEntity> newListCommon = new ArrayList<>();
                for(int i=0; i<list1.size(); i++){
                    int j=0;
                    for(; j<list2.size(); j++){
                        if(list1.get(i).getId().equals(list2.get(j).getId())){
                            newListCommon.add(list1.get(i));
                            break;
                        }
                    }
                    if(j==list2.size()){
                        newList1.add(list1.get(i));
                    }
                }
                for(int i=0; i<list2.size(); i++){
                    int j=0;
                    for(;j<list1.size();j++){
                        if(list2.get(i).getId().equals(list1.get(j).getId())){
                            break;
                        }
                    }
                    if(j==list1.size()){
                        newList2.add(list2.get(i));
                    }
                }
                object1.put("data", newList1);
                object1.put("itemsCount", newList1.size());
                object2.put("data", newList2);
                object2.put("itemsCount", newList2.size());
                objectCommon.put("data", newListCommon);
                objectCommon.put("itemsCount", newListCommon.size());
            }else if((list1!=null && list1.size()>0) && (list2==null || list2.size()==0)){
                object1.put("data", list1);
                object1.put("itemsCount", list1.size());
                object2.put("data", new ArrayList<>());
                object2.put("itemsCount", 0);
                objectCommon.put("data", new ArrayList<>());
                objectCommon.put("itemsCount", 0);
            }else if((list1==null || list1.size()==0) && (list2!=null && list2.size()>0)){
                object1.put("data", new ArrayList<>());
                object1.put("itemsCount", 0);
                object2.put("data", list2);
                object2.put("itemsCount", list2.size());
                objectCommon.put("data", new ArrayList<>());
                objectCommon.put("itemsCount", 0);
            }
        }
        object.put("project1", object1);
        object.put("project2", object2);
        object.put("projectCommon", objectCommon);
        return object;
    }
}
