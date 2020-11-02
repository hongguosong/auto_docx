package com.example.demo.service;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.service </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/18 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.BusinessException;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.config.security.VertifyHelper;
import com.example.demo.dao.*;
import com.example.demo.dto.ProjectVersionDto;
import com.example.demo.entity.*;
import com.example.demo.util.Encode;
import com.github.pagehelper.PageHelper;
import de.schlichtherle.license.LicenseContentException;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {
    @Value("${uploadPath}")
    private String uploadPath;

    @Resource
    private ProjectDao projectDao;

    @Resource
    private ProjectVersionDao projectVersionDao;

    @Resource
    private ProjectProcedureDao projectProcedureDao;

    @Resource
    private ProcedureDao procedureDao;

    @Resource
    private ProcedureVarDao procedureVarDao;

    @Resource
    private ProcedureCallDao procedureCallDao;

    @Resource
    private ProcedureParamDao procedureParamDao;

    @Resource
    private ProcedureVarCallDao procedureVarCallDao;

    @Resource
    private FunDescDao funDescDao;

    @Resource
    private TcfFileDao tcfFileDao;

    @Resource
    private CoverageFileDao coverageFileDao;

    @Resource
    private PictureFileDao pictureFileDao;

    @Resource
    private WordMubanDao wordMubanDao;

    @Resource
    private ReportDao reportDao;

    @Resource
    private ProblemDao problemDao;

    @Autowired
    private CurrentUserService currentUserService;

//    @Autowired
//    private FileUploadService fileUploadService;

    @Resource
    private LogDao logDao;

    public int addProject(ProjectEntity entity){
        List<ProjectEntity> list = projectDao.selectAll();
        for(ProjectEntity pe: list){
            if(pe.getGitAddress().equals(entity.getGitAddress())){
                throw new BusinessException("项目关联git地址重复");
            }
            if(pe.getName().equals(entity.getName())){
                throw new BusinessException("项目关联型号名称重复");
            }
        }

        String log = "创建“" + entity.getName() + "”项目";
        String createPerson = entity.getCreatePerson();
        Timestamp createTime = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDao.insertLog(log, createPerson, createTime);

        //编码
        entity.setGitPassword(Encode.encode(entity.getGitPassword()));
        return projectDao.insert(entity);
    }

    public int updateProject(ProjectEntity entity){
        String log = "修改“" + entity.getName() + "”项目";
        String createPerson = entity.getCreatePerson();
        Timestamp createTime = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDao.insertLog(log, createPerson, createTime);

        //编码
        entity.setGitPassword(Encode.encode(entity.getGitPassword()));
        return projectDao.updateProjectById(entity);
    }

    public List<ProjectEntity> getAllProject () {
        return projectDao.selectAll();
    }

    public ProjectEntity selectByGitAddress(String gitAddress) {
        List<ProjectEntity> list = projectDao.selectByGitAddress(gitAddress);
        for(ProjectEntity pe: list){
            if(pe.getDeleteId() == 2){
                return pe;
            }
        }
        return null;
    }

    public int addProjectVersion(ProjectVersionEntity entity) {
        List<ProjectVersionEntity> list = selectByVersionAndId(entity.getProjectId(),entity.getProjectVersion());
        if(list != null &&list.size() > 0){
            return 10;
        }
        if (!VertifyHelper.getInstance().vertify()) {
            throw new BusinessException("license has expired.");
        }
        int projectId = 0;
//        if(entity.getProjectId() == null){
//            projectId = entity.getProjectId();
//        }
        Date date = projectVersionDao.selectMaxDate(projectId);
        if(date != null && entity.getCreateTime().before(date)){
            throw new BusinessException("system time error.");
        }
        return projectVersionDao.insert(entity);
    }

    public List<ProjectVersionEntity> selectByProjectId(int projectId) {
        return projectVersionDao.selectByProjectId(projectId);
    }

    public List<ProjectVersionDto> selectProWidthLastVer(int pageIndex, int pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        List<ProjectVersionDto> list = projectDao.selectProWidthLastVer();
//        List<ProjectVersionDto> distinctList = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(ProjectVersionDto::getId)).descendingSet()), ArrayList::new));
//        return distinctList;
        return list;
    }

    public JSONObject selectProjectsByFunctionId(int pageIndex, int pageSize, int functionId){
        int startNum = pageIndex * pageSize;
        JSONObject object = new JSONObject();
        List<ProjectEntity> projectEntities = projectDao.selectProjectsByFunctionId(startNum, pageSize, functionId);
        //java8 根据id去除重复对象
        projectEntities = projectEntities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProjectEntity :: getId))), ArrayList::new));
        int itemsCount = projectDao.selectProjectsCountByFunctionId(functionId);
        object.put("data", projectEntities);
        object.put("itemsCount", itemsCount);
        return object;
    }

    public List<Map<String, Object>> selectVByFuncIdAndProId(int functionId, int projectId){
        List<ProjectVersionEntity> entityList = projectVersionDao.selectVByFuncIdAndProId(functionId, projectId);
        List<Map<String, Object>> versionList = new ArrayList<>();
        for(ProjectVersionEntity entity: entityList){
            Map<String, Object> version = new HashMap<>();
            version.put("name", entity.getProjectVersion());
            String commitString = entity.getCommits();
            JSONArray jsonArray = JSONArray.parseArray(commitString);
            List<Map<String, Object>> commitsList = new ArrayList<>();
            Map<String, Object> commits = new HashMap<>();
            for(int i=0; i<jsonArray.size(); i++){
                commits.put("message", jsonArray.getJSONObject(i).getString("message"));
                commits.put("timestamp", jsonArray.getJSONObject(i).getString("timestamp"));
                commits.put("author", jsonArray.getJSONObject(i).getJSONObject("author").getString("name"));
                commitsList.add(commits);
            }
            version.put("commitsList", commitsList);
            versionList.add(version);
        }
        return versionList;
    }

    public void deleteProject(int id){
        ProjectEntity project = projectDao.selectByPrimaryKey(id);
        projectDao.deleteProject(id);
        deleteNotOtherProjectUseFunction(id);
        Timestamp createTime = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        String userName = currentUserService.getUser().getUsername();
        logDao.insertLog("删除了项目"+project.getName(), userName, createTime);
    }

    public void deleteProjectVersionOthers(Integer project, String version){
        List<ReportEntity> reportEntityList = reportDao.findProcedureListByIdAndVersion(project,version);
        for(ReportEntity re: reportEntityList){
            reportDao.deleteByPrimaryKey(re.getId());
//            fileUploadService.deleteFile(new File(uploadPath + re.getUrl()));
//            fileUploadService.deleteFile(new File(uploadPath + re.getAddress()));
        }
        List<WordMubanEntity> wordMubanEntityList = wordMubanDao.selectByIdAndVersion(project,version);
        for(WordMubanEntity we: wordMubanEntityList){
            wordMubanDao.deleteByPrimaryKey(we.getId());
        }
        List<ProblemEntity> problemEntityList = problemDao.selectByProjectAndVersion(project,version);
        for(ProblemEntity pe: problemEntityList){
            problemDao.deleteByPrimaryKey(pe.getId());
        }
        List<ProjectVersionEntity> projectVersionEntityList = projectVersionDao.selectByVersionAndId(project,version);
        for(ProjectVersionEntity pve: projectVersionEntityList){
            projectVersionDao.deleteByPrimaryKey(pve.getId());
        }
        List<ProjectProcedureEntity> projectProcedureEntityList = projectProcedureDao.selectProjectProcedureList(project,version);
        for(ProjectProcedureEntity ppe: projectProcedureEntityList){
            projectProcedureDao.deleteByPrimaryKey(ppe.getId());
        }

        deletePackageAndVar();
        deletePackageAndVar();
    }

    public void deletePackageAndVar(){
        List<ProcedureEntity> procedureEntityList = procedureDao.findNotOtherUsePackage();
        for(ProcedureEntity pe: procedureEntityList){
            procedureDao.deleteByPrimaryKey(pe.getId());
            List<ProcedureVarEntity> procedureVarEntityList = procedureVarDao.selectByProcedureId(pe.getId());
            for(ProcedureVarEntity pve: procedureVarEntityList){
                procedureVarDao.deleteByPrimaryKey(pve.getId());
            }
        }
    }

    public void deleteProcedureAndCorrelation(List<ProcedureEntity> list){
        if(list != null){
            for(ProcedureEntity pe: list){
                procedureDao.deleteByPrimaryKey(pe.getId());
                tcfFileDao.deleteByFunctionNameAndHashValue(pe.getLongName(),pe.getFullName(),pe.getHashValue());
                coverageFileDao.deleteByFunctionNameAndHashValue(pe.getLongName(),pe.getFullName(),pe.getHashValue());
                pictureFileDao.deleteByFunctionNameAndHashValue(pe.getLongName(),pe.getFullName(),pe.getHashValue());
                List<ProcedureCallEntity> procedureCallEntityList = procedureCallDao.selectByProcedureId(pe.getId());
                for(ProcedureCallEntity pce: procedureCallEntityList){
                    procedureCallDao.deleteByPrimaryKey(pce.getId());
                }
                List<ProcedureParamEntity> procedureParamEntityList = procedureParamDao.findProcedureParamByProcedureId(pe.getId());
                for(ProcedureParamEntity ppe: procedureParamEntityList){
                    procedureParamDao.deleteByPrimaryKey(ppe.getId());
                }
                List<ProcedureVarCallEntity> procedureVarCallEntityList = procedureVarCallDao.selectByProcedureId(pe.getId());
                for(ProcedureVarCallEntity pvce: procedureVarCallEntityList){
                    procedureVarCallDao.deleteByPrimaryKey(pvce.getId());
                }
                List<FunDescEntity> funDescEntityList = funDescDao.selectByProcedureId(pe.getId());
                for(FunDescEntity fde: funDescEntityList){
                    funDescDao.deleteByPrimaryKey(fde.getId());
                }
            }
        }
    }

    public void deleteProjectVersion(Integer project, String version){
        ProjectEntity entity = projectDao.selectByPrimaryKey(project);
        List<ProcedureEntity> list = procedureDao.findNotOtherProjectVersionUse(project,version);
        deleteProcedureAndCorrelation(list);
        deleteProjectVersionOthers(project,version);
        Timestamp createTime = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        String userName = currentUserService.getUser().getUsername();
        logDao.insertLog("删除了项目"+entity.getName()+"的版本"+version, userName, createTime);
    }

    public List<ProjectVersionEntity> selectByVersionAndId(Integer project, String version){

        List<ProjectVersionEntity> res = projectVersionDao.selectByVersionAndId(project, version);
        if(res == null){
            res = new ArrayList<>();
        }
        return res;
    }

    public void deleteNotOtherProjectUseFunction(Integer projectId){
        List<ProcedureEntity> list = procedureDao.findNotOtherProjectUse(projectId);
        deleteProcedureAndCorrelation(list);
        List<ProjectVersionEntity> projectVersionEntityList = selectByProjectId(projectId);
        for(ProjectVersionEntity pve: projectVersionEntityList){
            deleteProjectVersionOthers(pve.getProjectId(), pve.getProjectVersion());
        }
    }
}
