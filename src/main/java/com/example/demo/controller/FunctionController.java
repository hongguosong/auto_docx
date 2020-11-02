package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.dto.ProcedureCallDto;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.*;
import com.example.demo.service.PermissionService;
import com.example.demo.service.ProcedureService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@RestController
@RequestMapping("function")
public class FunctionController {
    @Autowired
    private ProcedureService procedureService;

    @PostMapping("/getProVerFuc")
    public Map<String, Object> getProVerFuc(@RequestBody JSONObject requestJson,
                                                  @RequestParam(name = "project") Integer project,
                                                  @RequestParam(name = "version") String version){
        PageInfo<ProcedureDto> pageInfo = new PageInfo(procedureService.findProcedureListByIdAndVersion(project,version,requestJson));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }
    @PostMapping("/getProVerFucCount")
    public String getProVerFucCount(@RequestParam(name = "project") Integer project,
                                            @RequestParam(name = "version") String version){
        Integer old = procedureService.selectProcedureCount(project, version, 0);
        Integer modify = procedureService.selectProcedureCount(project, version, 1);
        Integer add = procedureService.selectProcedureCount(project, version, 2);
        Integer all = old + modify + add;
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("all", all);
        returnData.put("old", old);
        returnData.put("modify", modify);
        returnData.put("add", add);
        return JsonResult.success(returnData);
    }

    @PostMapping("/getProVerFucFull")
    public Map<String, Object> getProVerFucFull(@RequestBody JSONObject requestJson,
                                            @RequestParam(name = "project") Integer project,
                                            @RequestParam(name = "version") String version){
        PageInfo<ProcedureDto> pageInfo = new PageInfo(procedureService.findProcedureListByIdAndVersionFull(project,version,requestJson));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @PostMapping("/findProjectByProcedureId")
    public String findProjectByProcedureId(@RequestParam(name="procedureId") Integer procedureId){
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findProjectByProcedureId(procedureId));
        return JsonResult.success(list);
    }

    @PostMapping("/findProcedureParamByProcedureId")
    public String findProcedureParamByProcedureId(@RequestParam(name="procedureId") Integer procedureId){
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findProcedureParamByProcedureId(procedureId));
        return JsonResult.success(list);
    }

    @PostMapping("/findProcedureVarByProcedureId")
    public String findProcedureVarByProcedureId(@RequestParam(name="procedureId") Integer procedureId){
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findProcedureVarByProcedureId(procedureId));
        return JsonResult.success(list);
    }

    public ProcedureCallDto getDistinctList(List<ProcedureCallDto> child, List<ProcedureCallDto> parent, ProcedureCallDto self){
        List<ProcedureCallDto> list = new ArrayList<>();
        list.addAll(child);
        list.addAll(parent);
        list.add(self);
        for(int i=0; i< list.size(); i++){
            int num=0;
            for(int j=0; j<list.size(); j++){
                if(list.get(i).getName().equals(list.get(j).getName())){
                    num++;
                }
            }
            if(num>=2){
                String tmp = list.get(i).getName();
                for(ProcedureCallDto dto: list){
                    if(dto.getName().equals(tmp)){
//                        if(dto.getFullName().contains("(")){
//                            dto.setName(dto.getFullName().substring(dto.getFullName().lastIndexOf(".")+1,dto.getFullName().length()));
//                        }else{
//                            dto.setName(dto.getFullName());
//                        }
//                        dto.setName(dto.getFullName());
                        dto.setName(dto.getFullName().substring(dto.getFullName().lastIndexOf(".")+1,dto.getFullName().length()));
                    }
                }
            }
        }
        return list.get(list.size()-1);
    }

    @PostMapping("/getProcedureCall")
    public Map<String, Object> getProcedureCallRelation(@RequestParam(name="procedureId") Integer procedureId){
        ProcedureEntity procedureEntity = procedureService.findProcedureById(procedureId);
        ProcedureCallDto dto = new ProcedureCallDto();
        dto.setVersion(procedureEntity.getVersion());
        dto.setName(procedureEntity.getName()+"_v"+procedureEntity.getVersion());
        dto.setFullName(procedureEntity.getFullName()+"_v"+procedureEntity.getVersion());
        dto.setLongName(procedureEntity.getLongName()+"_v"+procedureEntity.getVersion());
        dto.setId(procedureEntity.getId());
        List<ProcedureCallDto> child = procedureService.findProcedureCallChildByProcedureId(procedureId);
        for(ProcedureCallDto pDto: child){
            pDto.setName(pDto.getName()+"_v"+pDto.getVersion());
            pDto.setFullName(pDto.getFullName()+"_v"+pDto.getVersion());
        }
        List<ProcedureCallDto> parent = procedureService.findProcedureCallParentByProcedureId(procedureId);
        for(ProcedureCallDto pDto: parent){
            pDto.setName(pDto.getName()+"_v"+pDto.getVersion());
            pDto.setFullName(pDto.getFullName()+"_v"+pDto.getVersion());
        }
        // dto = getDistinctList(child, parent, dto);
        Map<String, Object> result = new HashMap<>();
        result.put("self",dto);
        result.put("parent", parent);
        result.put("child", child);
        return result;
    }

    @PostMapping("/getProVerProcedureCall")
    public Map<String, Object> getProVerProcedureCallRelation(@RequestParam(name="procedureId") Integer procedureId,
                                                              @RequestParam(name = "project") Integer project,
                                                              @RequestParam(name = "version") String version){
        ProcedureEntity procedureEntity = procedureService.findProcedureById(procedureId);
        ProcedureCallDto dto = new ProcedureCallDto();
        dto.setName(procedureEntity.getName());
        dto.setFullName(procedureEntity.getFullName());
        dto.setId(procedureEntity.getId());
        dto.setLongName(procedureEntity.getLongName());
        List<ProcedureCallDto> child = procedureService.findProVerProcedureCallChildByProcedureId(procedureId, project, version);
        List<ProcedureCallDto> parent = procedureService.findProVerProcedureCallParentByProcedureId(procedureId, project, version);
        // dto = getDistinctList(child, parent, dto);
        Map<String, Object> result = new HashMap<>();
        result.put("self",dto);
        result.put("parent", parent);
        result.put("child", child);
        return result;
    }

    @PostMapping("/selectProcedureAll")
    public Map<String, Object> selectProcedureAll(@RequestBody JSONObject requestJson){
        PageInfo<ProcedureEntity> pageInfo = new PageInfo<>(procedureService.selectProcedureAll(requestJson));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }

    @PostMapping("/findCoverageFileByProcedureFullName")
    public String findCoverageFileByProcedureFullName (@RequestParam(name = "functionName") String functionName,
                                                                         @RequestParam(name = "project") Integer project,
                                                                         @RequestParam(name = "version") String version,
                                                                         @RequestParam(name = "hashValue") String hashValue) {
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findCoverageFileByProcedureFullName(functionName, project, version, hashValue));
        return JsonResult.success(list);
    }
    
    @PostMapping("/findTcfFileByProcedureFullName")
    public String findTcfFileByProcedureFullName (@RequestParam(name = "functionName") String functionName,
                                                               @RequestParam(name = "project") Integer project,
                                                               @RequestParam(name = "version") String version,
                                                               @RequestParam(name = "hashValue") String hashValue) {
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findTcfFileByProcedureFullName(functionName, project, version, hashValue));
        return JsonResult.success(list);
    }

    @PostMapping("/findPictureFileByProcedureFullName")
    public String findPictureFileByProcedureFullName (@RequestParam(name = "functionName") String functionName,
                                                                       @RequestParam(name = "project") Integer project,
                                                                       @RequestParam(name = "version") String version,
                                                                       @RequestParam(name = "hashValue") String hashValue) {
        Map<String,Object> list = new HashMap<>();
        list.put("list", procedureService.findPictureFileByProcedureFullName(functionName, project, version, hashValue));
        return JsonResult.success(list);
    }

    @PostMapping("/isOverloadedFun")
    public String isOverloadedFun (@RequestParam(name = "longName") String longName,
                                                      @RequestParam(name = "project") Integer project,
                                                      @RequestParam(name = "version") String version) {
        Map<String,Object> list = new HashMap<>();
        list.put("isOverloaded", procedureService.isOverloadedFun(project, version, longName));
        return JsonResult.success(list);
    }
}
