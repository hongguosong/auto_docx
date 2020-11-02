package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.analyzer.AnalyzerDatabase;
import com.example.demo.analyzer.LexicalElement;
import com.example.demo.analyzer.VarItem;
import com.example.demo.dao.*;
import com.example.demo.dto.ProcedureCallDto;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.dto.ProjectVersionDto;
import com.example.demo.entity.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class ProcedureService {
    private static final int INSERT_FLAG_NOT_CHANGED = 0; //0-未修改
    private static final int INSERT_FLAG_MODIFIED = 1; //1-修改
    private static final int INSERT_FLAG_NEW = 2; //2-新增

    @Autowired
    private ProcedureDao procedureDao;
    @Autowired
    private ProcedureCallDao procedureCallDao;
    @Autowired
    private ProcedureParamDao procedureParamDao;
    @Autowired
    private ProcedureVarDao procedureVarDao;
    @Autowired
    private ProcedureVarCallDao procedureVarCallDao;
    @Autowired
    private ProjectProcedureDao projectProcedureDao;
    @Autowired
    private CoverageFileDao coverageFileDao;
    @Autowired
    private PictureFileDao pictureFileDao;
    @Autowired
    private TcfFileDao tcfFileDao;
    @Autowired
    private ProjectVersionDao projectVersionDao;
    @Autowired
    private FunDescService funDescService;

    public List<ProjectProcedureEntity> selectProjectProcedureList(Integer projectId, String projectVersion){
        return projectProcedureDao.selectProjectProcedureList(projectId, projectVersion);
    }

    public void updateProcedureDatabase(Integer projectId, String projectVersion, AnalyzerDatabase db){
        LexicalElement root = db.getRoot();
        Integer parentId = null;

        List<ProjectProcedureEntity> projectProcedureList = projectProcedureDao.selectProjectProcedureList(projectId, projectVersion);
        if(projectProcedureList.size() == 0) {
            for (LexicalElement child : root.getChildren()) {
                updateProcedure(projectId, projectVersion, parentId, child);
            }
            for (LexicalElement child : root.getChildren()) {
                updateCall(projectId,projectVersion,child);
            }
        }
    }

    public void updateProcedure(Integer projectId, String projectVersion, Integer parentId, LexicalElement item){
        Integer id = null;
        if(item.getName() != null && !item.getName().isEmpty()){
            List<ProcedureEntity> foundProcList = null;
            String fullName = item.getFullName();
            String hashValue = item.getHashValue();
            switch (item.getType()){
                case LexicalElement.TYPE_PACKAGE:
                case LexicalElement.TYPE_PROT:
                    foundProcList = procedureDao.findProcedureListByFullName(fullName);
                    if(foundProcList.size() == 0){
                        id = insertProcedureInfo(parentId, item, 1);
                    }
                    else{
                        id = foundProcList.get(0).getId();
                        //更新全局变量
                        List<ProcedureVarEntity> procedureVarEntityList= procedureVarDao.selectByProcedureId(id);
                        for(VarItem var : item.getVars()){
                            ProcedureVarEntity pve = new ProcedureVarEntity();
                            pve.setProcedureId(id);
                            pve.setName(var.getName());
                            pve.setCls(var.getCls());
                            pve.setVal(var.getVal());
                            if(var.getVal() != null && var.getVal().length() > 100){
                                pve.setVal(var.getVal().substring(0, 100));
                            }
                            int i=0;
                            for(;i<procedureVarEntityList.size(); i++){
                                if(pve.getName().equals(procedureVarEntityList.get(i).getName())){
                                    break;
                                }
                            }
                            if(i==procedureVarEntityList.size()){
                                procedureVarDao.insert(pve);
                            }
                        }
                    }
                    break;
                case LexicalElement.TYPE_PROC:
                case LexicalElement.TYPE_FUNC:
                case LexicalElement.TYPE_TASK:
                    foundProcList = procedureDao.findProcedureListByHashValue(fullName, hashValue);
                    if(foundProcList.size() > 0){
                        id = foundProcList.get(0).getId();
                        ProjectProcedureEntity ppe = new ProjectProcedureEntity();
                        ppe.setProjectId(projectId);
                        ppe.setProjectVersion(projectVersion);
                        ppe.setProcedureId(id);
                        ppe.setInsertFlag(INSERT_FLAG_NOT_CHANGED);
                        projectProcedureDao.insert(ppe);
                    }
                    else {
                        foundProcList = procedureDao.findProcedureListByFullName(fullName);
                        int version;
                        if(foundProcList.size() > 0){
                            version = foundProcList.get(foundProcList.size() - 1).getVersion() + 1;
                            id = insertProcedureInfo(parentId, item, version);
                            ProjectProcedureEntity ppe = new ProjectProcedureEntity();
                            ppe.setProjectId(projectId);
                            ppe.setProjectVersion(projectVersion);
                            ppe.setProcedureId(id);
                            ppe.setInsertFlag(INSERT_FLAG_MODIFIED);
                            projectProcedureDao.insert(ppe);
                        }
                        else{
                            version = 1;
                            id = insertProcedureInfo(parentId, item, version);
                            ProjectProcedureEntity ppe = new ProjectProcedureEntity();
                            ppe.setProjectId(projectId);
                            ppe.setProjectVersion(projectVersion);
                            ppe.setProcedureId(id);
                            ppe.setInsertFlag(INSERT_FLAG_NEW);
                            projectProcedureDao.insert(ppe);
                        }
                    }
                    break;
            }
        }

        if(id != null) {
            for (LexicalElement child : item.getChildren()) {
                updateProcedure(projectId, projectVersion, id, child);
            }
        }
    }

    public void updateCall(Integer projectId,String projectVersion, LexicalElement item){
        if(item.getName() != null && !item.getName().isEmpty()){
            List<ProcedureDto> foundProcList = null;
            String fullName = item.getFullName();
            String hashValue = item.getHashValue();
            switch (item.getType()){
                case LexicalElement.TYPE_PROC:
                case LexicalElement.TYPE_FUNC:
                case LexicalElement.TYPE_TASK:
//                    foundProcList = procedureDao.findProcedureListByPVAndAndFullNameHashValue(projectId,projectVersion,fullName, hashValue);
//                    if(foundProcList.size() > 0){
//                        ProcedureDto pd = foundProcList.get(0);
//                        if(pd.getInsertFlag() == 1 || pd.getInsertFlag() == 2){ //修改或新增函数需要更新调用关系
//                            insertCallInfo(projectId,projectVersion,item);
//                        }
//                    }
                    insertCallInfo(projectId,projectVersion,item);
                    break;
                default:
                    break;
            }
        }

        for (LexicalElement child : item.getChildren()) {
            updateCall(projectId,projectVersion,child);
        }
    }

    public Integer insertProcedureInfo(Integer parentId, LexicalElement item, int version){
        if(item.getName() != null && !item.getName().isEmpty()){
            List<ProcedureEntity> foundProcList = null;
            int type = item.getType();
            String hashValue = item.getHashValue();
            String returnType = item.getReturnType();
            Integer lineCount = item.getLineCount();
            String comment = null; //item.getComment()
            if(type == LexicalElement.TYPE_PACKAGE){
                hashValue = null;
                returnType = null;
                lineCount = null;
            }
            else if(type == LexicalElement.TYPE_PROC){
                returnType = null;
            }
            ProcedureEntity procedure = new ProcedureEntity();
            procedure.setName(item.getName());
            procedure.setLongName(item.getLongName());
            procedure.setFullName(item.getFullName());
            procedure.setType(item.getType());
            procedure.setVersion(version);
            procedure.setHashValue(hashValue);
            procedure.setReturnType(returnType);
            procedure.setLineCount(lineCount);
            procedure.setComment(comment);
            procedure.setParentId(parentId);
            procedure.setCreateTime(new Date());
            procedure.setUpdateTime(new Date());
            procedureDao.insert(procedure);
            if(type == LexicalElement.TYPE_PROC || type == LexicalElement.TYPE_FUNC || type == LexicalElement.TYPE_TASK){
                //添加空的函数描述信息
                FunDescEntity funDescEntity = new FunDescEntity();
                funDescEntity.setComment("");
                funDescEntity.setInputDescription("");
                funDescEntity.setOutputDescription("");
                funDescEntity.setName(procedure.getName());
                funDescEntity.setVarDescription("");
                funDescEntity.setProcedureId(procedure.getId());
                funDescService.add(funDescEntity);
            }
            Integer id = procedure.getId();
//            for(LexicalElement call : item.getCalls()){
//                List<ProcedureEntity> callList = procedureDao.findProcedureListByHashValue(call.getFullName(), call.getHashValue());
//                if(callList.size() > 0){
//                    ProcedureCallEntity pce = new ProcedureCallEntity();
//                    pce.setProcedureId(id);
//                    pce.setCalledId(callList.get(0).getId());
//                    procedureCallDao.insert(pce);
//                }
//            }
//            for(VarItem call : item.getVarCalls()){
//                List<ProcedureEntity> callList = procedureDao.findProcedureListByFullName(call.getParent().getFullName()); //PACKAGE的HASH值为空
//                if(callList.size() > 0){
//                    List<ProcedureVarEntity> varList = procedureVarDao.selectByProcedureId(callList.get(0).getId());
//                    ProcedureVarEntity calledVar = null;
//                    for(ProcedureVarEntity var : varList){
//                        if(var.getName().toLowerCase().equals(call.getName().toLowerCase())){
//                            calledVar = var;
//                            break;
//                        }
//                    }
//                    if(calledVar != null){
//                        ProcedureVarCallEntity pvce = new ProcedureVarCallEntity();
//                        pvce.setProcedureId(id);
//                        pvce.setCalledVarId(calledVar.getId());
//                        procedureVarCallDao.insert(pvce);
//                    }
//                }
//            }
            for(VarItem param : item.getParams()){
                ProcedureParamEntity ppe = new ProcedureParamEntity();
                ppe.setProcedureId(id);
                ppe.setName(param.getName());
                ppe.setCls(param.getCls());
                ppe.setType(param.getType());
                ppe.setVal(param.getVal());
                if(param.getVal() != null && param.getVal().length() > 100){
                    ppe.setVal(param.getVal().substring(0, 100));
                }
                procedureParamDao.insert(ppe);
            }
            for(VarItem var : item.getVars()){
                ProcedureVarEntity pve = new ProcedureVarEntity();
                pve.setProcedureId(id);
                pve.setName(var.getName());
                pve.setCls(var.getCls());
                pve.setVal(var.getVal());
                if(var.getVal() != null && var.getVal().length() > 100){
                    pve.setVal(var.getVal().substring(0, 100));
                }
                procedureVarDao.insert(pve);
            }

            return id;
        }
        return null;
    }

    public void insertCallInfo(Integer projectId, String projectVersion, LexicalElement item){
        //List<ProcedureDto> foundProcItemList = procedureDao.findProcedureListByPVAndAndFullNameHashValue(projectId,projectVersion,item.getFullName(), item.getHashValue());
        Integer id;
        List<ProcedureEntity> foundProcList = procedureDao.findProcedureListByHashValue(item.getFullName(), item.getHashValue());
        if(foundProcList.size() > 0){
            id = foundProcList.get(0).getId();
            for(LexicalElement call : item.getCalls()){
                List<ProcedureEntity> callList = procedureDao.findProcedureListByHashValue(call.getFullName(), call.getHashValue());
                //List<ProcedureDto> foundProcItemCallList = procedureDao.findProcedureListByPVAndAndFullNameHashValue(projectId,projectVersion,call.getFullName(), call.getHashValue());
                if(callList.size() > 0){
//                    if(foundProcItemList.get(0).getInsertFlag() == 1 || foundProcItemList.get(0).getInsertFlag() == 2 ||
//                            foundProcItemCallList.get(0).getInsertFlag() == 1 || foundProcItemCallList.get(0).getInsertFlag() == 2){
//
//                    }
                    ProcedureCallEntity pce = new ProcedureCallEntity();
                    pce.setProcedureId(id);
                    pce.setCalledId(callList.get(0).getId());
                    List<ProcedureCallEntity> existProcedureCallList = procedureCallDao.selectByProcedureIdAndCallId(pce.getProcedureId(),pce.getCalledId());
                    if(existProcedureCallList == null || existProcedureCallList.size() == 0){
                        procedureCallDao.insert(pce);
                    }
                }
            }
            for(VarItem call : item.getVarCalls()){
                List<ProcedureEntity> callList = procedureDao.findProcedureListByFullName(call.getParent().getFullName()); //PACKAGE的HASH值为空
                if(callList.size() > 0){
                    List<ProcedureVarEntity> varList = procedureVarDao.selectByProcedureId(callList.get(0).getId());
                    ProcedureVarEntity calledVar = null;
                    for(ProcedureVarEntity var : varList){
                        if(var.getName().toLowerCase().equals(call.getName().toLowerCase())){
                            calledVar = var;
                            break;
                        }
                    }
                    if(calledVar != null){
                        ProcedureVarCallEntity pvce = new ProcedureVarCallEntity();
                        pvce.setProcedureId(id);
                        pvce.setCalledVarId(calledVar.getId());
                        List<ProcedureVarCallEntity> existVarCallList = procedureVarCallDao.selectByProcedureIdAndCalledVarId(pvce.getProcedureId(),pvce.getCalledVarId());
                        if(existVarCallList == null || existVarCallList.size() == 0){
                            procedureVarCallDao.insert(pvce);
                        }
                    }
                }
            }
        }
    }

    public List<ProcedureDto> findProcedureListByIdAndVersion(Integer project, String version, JSONObject jo){
        int pageIndex = jo.containsKey("pageIndex") ? jo.getInteger("pageIndex") : 1;
        int pageSize = jo.containsKey("pageSize") ? jo.getInteger("pageSize") : 20;
        PageHelper.startPage(pageIndex, pageSize);
        String sortField = "";
        if(jo.containsKey("sortField")){
            sortField = jo.getString("sortField");
            if(sortField.contains("longName")){
                sortField = "r.long_name";
            }else if(sortField.contains("lineCount")){
                sortField = "r.line_count";
            }else if(sortField.contains("insertFlag")){
                sortField = "p.insert_flag";
            }else {
                sortField = "";
            }
        }
        List<ProcedureDto> list = procedureDao.findProcedureListByIdAndVersion(project,version,
                sortField, jo.containsKey("sortOrder") ? jo.getString("sortOrder") : "",
                jo.containsKey("name") ? jo.getString("name") : "",
                jo.containsKey("longName") ? jo.getString("longName") : "",jo.containsKey("fullName") ? jo.getString("fullName") : "",
                jo.containsKey("version") ? jo.getString("version") : "",jo.containsKey("hashValue") ? jo.getString("hashValue") : "",
                jo.containsKey("returnType") ? jo.getString("returnType") : "",
                jo.containsKey("lineCount") ? jo.getString("lineCount") : "",
                jo.containsKey("comment") ? jo.getString("comment") : "",
                jo.containsKey("returnComment") ? jo.getString("returnComment") : "",
                jo.containsKey("parentId") ? jo.getString("parentId") : "",jo.containsKey("creatTime") ? jo.getString("creatTime") : "",
                jo.containsKey("updateTime") ? jo.getString("updateTime") : "",jo.containsKey("insertFlag") ? jo.getInteger("insertFlag") : -1,
                jo.containsKey("affirm") ? jo.getInteger("affirm") : -1);
        return list;
    }

    public int selectProcedureCount(Integer project, String version, Integer insertFlag){

        Integer res = procedureDao.selectProcedureCount(project,version,insertFlag);
        return res;
    }

    public List<ProcedureDto> findProcedureListByIdAndVersionFull(Integer project, String version, JSONObject jo){
        int pageIndex = jo.containsKey("pageIndex") ? jo.getInteger("pageIndex") : 1;
        int pageSize = jo.containsKey("pageSize") ? jo.getInteger("pageSize") : 20;
        PageHelper.startPage(pageIndex, pageSize);
        List<ProcedureDto> list = procedureDao.findProcedureListByIdAndVersionFull(project,version,
                jo.containsKey("tcf") ? jo.getString("tcf") : "",
                jo.containsKey("coverage") ? jo.getString("coverage") : "",
                jo.containsKey("picture") ? jo.getString("picture") : "",
                jo.containsKey("problem") ? jo.getString("problem") : "",
                jo.containsKey("insertFlag") ? jo.getInteger("insertFlag") : -1);
        return list;
    }

    public List<ProjectEntity> findProjectByProcedureId(Integer procedureId){
        List<ProjectEntity> list = procedureDao.findProjectByProcedureId(procedureId);
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProjectEntity :: getId))), ArrayList::new));
        return list;
    }

    public List<ProcedureParamEntity> findProcedureParamByProcedureId(Integer procedureId){
        return procedureParamDao.findProcedureParamByProcedureId(procedureId);
    }

    public List<ProcedureVarEntity> findProcedureVarByProcedureId(Integer procedureId){
        return procedureVarDao.findProcedureVarByProcedureId(procedureId);
    }

    public List<ProcedureCallDto> findProcedureCallChildByProcedureId(Integer procedureId){
        return procedureCallDao.findProcedureCallChildByProcedureId(procedureId);
    }

    public List<ProcedureCallDto> findProVerProcedureCallChildByProcedureId(Integer procedureId, Integer project, String version){
        return procedureCallDao.findProVerProcedureCallChildByProcedureId(procedureId, project, version);
    }

    public List<ProcedureCallDto> findProcedureCallParentByProcedureId(Integer procedureId){
        return procedureCallDao.findProcedureCallParentByProcedureId(procedureId);
    }

    public List<ProcedureCallDto> findProVerProcedureCallParentByProcedureId(Integer procedureId, Integer project, String version){
        return procedureCallDao.findProVerProcedureCallParentByProcedureId(procedureId, project, version);
    }

    public ProcedureEntity findProcedureById(Integer procedureId){
        return procedureDao.selectByPrimaryKey(procedureId);
    }

    public List<ProcedureEntity> selectProcedureAll(JSONObject jo){
        int pageIndex = jo.containsKey("pageIndex") ? jo.getInteger("pageIndex") : 1;
        int pageSize = jo.containsKey("pageSize") ? jo.getInteger("pageSize") : 20;
        PageHelper.startPage(pageIndex, pageSize);
        List<ProcedureEntity> list = procedureDao.selectProcedureAll(jo.containsKey("name") ? jo.getString("name") : "",jo.containsKey("longName") ? jo.getString("longName") : "",
                jo.containsKey("fullName") ? jo.getString("fullName") : "",
                jo.containsKey("version") ? jo.getString("version") : "",jo.containsKey("hashValue") ? jo.getString("hashValue") : "",
                jo.containsKey("returnType") ? jo.getString("returnType") : "",jo.containsKey("lineCount") ? jo.getInteger("lineCount") : null,
                jo.containsKey("comment") ? jo.getString("comment") : "", jo.containsKey("returnComment") ? jo.getString("returnComment") : "",
                jo.containsKey("parentId") ? jo.getString("parentId") : "",jo.containsKey("creatTime") ? jo.getString("creatTime") : "",
                jo.containsKey("updateTime") ? jo.getString("updateTime") : "");
        //List<ProcedureEntity> distinctList = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(ProcedureEntity::getFullName))), ArrayList::new));
        return list;
    }

    public List<CoverageFileEntity> findCoverageFileByProcedureFullName (String functionName, Integer project, String version, String hashValue) {
        // return coverageFileDao.findCoverageFileByProcedureFullName(functionName, project, version);
        return coverageFileDao.findCoverageFileByFuncName(functionName.toLowerCase(), hashValue);
    }

    public List<TcfFileEntity> findTcfFileByProcedureFullName (String functionName, Integer project, String version, String hashValue) {
        return tcfFileDao.findTcfFileByFuncName(functionName.toLowerCase(), hashValue);
        // return tcfFileDao.findTcfFileByProcedureFullName(functionName, project, version);
    }

    public List<PictureFileEntity> findPictureFileByProcedureFullName (String functionName, Integer project, String version, String hashValue) {
        // return pictureFileDao.findPictureFileByProcedureFullName(functionName, project, version);
        return pictureFileDao.findPictureFileByFuncName(functionName.toLowerCase(), hashValue);
    }

    public List<ProcedureDto> selectFunctionsByProjectId(int projectId, String projectVersion){
        return procedureDao.selectFunctionsByProjectId(projectId, projectVersion);
    }

    public List<ProcedureDto> getFuncByProIdPagination(int projectId, int startNum, int pageSize){
        //获取当前该项目的最新版本
        List<ProjectVersionEntity> projectVersionEntities = projectVersionDao.selectByProjectId(projectId);
        projectVersionEntities = projectVersionEntities.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(ProjectVersionEntity::getProjectId))), ArrayList::new));
        if(projectVersionEntities.size() > 0){
            String projectVersion = projectVersionEntities.get(0).getProjectVersion();
            return procedureDao.selectFunctionsByProjectId2(startNum, pageSize, projectId, projectVersion);
        }else{
            return new ArrayList<ProcedureDto>();
        }
    }

    public List<ProcedureDto> getFuncCallByProAndV(int projectId, String projectVersion){
        if(projectVersion.equals("null")){
            return new ArrayList<ProcedureDto>();
        }
        List<ProcedureDto> funcCalledList = procedureDao.getFuncCallByProAndV(projectId, projectVersion);
        //去重funcCalledList
        List<ProcedureDto> funcCalledSingleList = funcCalledList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(ProcedureDto::getId))), ArrayList::new));
        List<ProcedureDto> procedureDtoList = procedureDao.selectFunctionsByProjectId(projectId, projectVersion);
        //根据id求差集 funcCalledSingleList 和 procedureDtoList 得到根节点
        List<ProcedureDto> rootFunc = procedureDtoList.stream().filter(item -> !funcCalledSingleList.stream().map(ProcedureDto::getId).collect(Collectors.toList()).contains(item.getId())).collect(Collectors.toList());
        //将根节点放入 funcCalledList 组成一个新的List
        funcCalledList.add(rootFunc.get(0));
        return funcCalledList;
    }

    public boolean isOverloadedFun(int projectId, String projectVersion, String longName){
        List<ProcedureEntity> list = procedureDao.getOverloadedFunByLongName(projectId, projectVersion, longName);
        if(list !=null && list.size() >= 2){
            return true;
        }
        return false;
    }
}
