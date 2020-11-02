package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/4 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.dao.FunDescDao;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.dto.ReportDto;
import com.example.demo.email.service.EmailService;
import com.example.demo.entity.FunDescEntity;
import com.example.demo.entity.ReportEntity;
import com.example.demo.service.*;
import com.example.demo.task.ProduceReportTask;
import com.github.pagehelper.PageInfo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/report")
@RestController
public class ReportController {
    @Resource
    private FunDescDao funDescDao;
    @Resource
    private ProcedureService procedureService;

    @Autowired
    private LogService logService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private FunDescService funDescService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/produce")
    public String produceReport(HttpServletRequest request,
                                @RequestBody JSONObject jsonObject,
                                @RequestParam("project") Integer project,
                                @RequestParam("version") String version,
                                @RequestParam(name = "fileType") Integer fileType,
                                @RequestParam("projectName") String projectName,
                                @RequestParam("sure") String sure) throws IOException, InvalidFormatException{
        ReportDto report = new ReportDto();
        report.setName(jsonObject.getString("reportName"));
        report.setDescription(jsonObject.getString("reportDesc"));
        report.setProjectName(projectName);
        report.setCreatePerson(currentUserService.getUser().getUsername());
        if(report.getCreatePerson() == null || report.getCreatePerson().isEmpty()) {
            return JsonResult.failed("登录已过期, 请重新登录.");
        }
        report.setTo(currentUserService.getUser().getEmail());
        report.setProjectId(project);
        report.setProjectVersion(version);
        report.setFileType(fileType);

//        List<FunDescEntity> funDescEntities = funDescService.selectByProjectIdAndVersion(project, version);
//        for(FunDescEntity funDescEntity: funDescEntities){
//            if(funDescEntity.getComment() == null || funDescEntity.getComment().isEmpty()){
//                return JsonResult.failed("存在未确认函数，请先确认.");
//            }
//        }

        String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/file-upload/downloadReport?";
        report.setEmailLink(url);
        for(int i=0; i<ProduceReportTask.reportList.size();i++){
            ReportDto dto = ProduceReportTask.reportList.get(i);
            if(dto.getProjectId() == report.getProjectId() &&
                    dto.getProjectVersion().equals(report.getProjectVersion()) &&
                    dto.getCreatePerson().equals(report.getCreatePerson()) &&
                    dto.getFileType() == report.getFileType()){
                return JsonResult.failed("该报告正在生成中.");
            }
        }

        if("1".equals(sure)){
            Configure config = Configure.newBuilder().build();
            XWPFTemplate template = XWPFTemplate.compile(reportService.getWordMubanUrl(project, version, report.getFileType()), config);
            List<Map<String, String>> mubanFunctionList = new ArrayList<>();
            List<ProcedureDto> procedureDtoList = procedureService.selectFunctionsByProjectId(project,version);
            reportService.getMubanFunctionList(template.getXWPFDocument(), report.getFileType(), mubanFunctionList, procedureDtoList);
            template.close();
            for(Map<String, String> fun: mubanFunctionList){
                List<FunDescEntity> funDescEntityList = funDescDao.selecByName(fun.get("name").toLowerCase(), project, version);
                if(funDescEntityList != null && funDescEntityList.size()>0){
                    String desc = funDescEntityList.get(0).getComment();
                    if(desc == null || desc.isEmpty()){
                        return JsonResult.failed("存在未确认函数["+fun.get("name")+"]，请先确认.");
                    }
                }else{
                    return JsonResult.failed("报告文件中存在未知函数["+fun.get("name")+"]，请先检查报告文件.");
                }
            }
        }

        if("1".equals(sure)){
            Map<String, Object> res = new HashMap<>();
            res.put("data", "2");
            return JsonResult.success(res);
        }else if("2".equals(sure)){
            ProduceReportTask.putIfAbsent(report);
            Map<String, Object> res = new HashMap<>();
            res.put("data", "3");
            return JsonResult.success(res);
        }else{
            return JsonResult.success();
        }
    }

    @PostMapping("/selectAllReport")
    public Map<String, Object> selectAllReport(@RequestBody JSONObject jsonObject,
                                               @RequestParam("project") Integer project,
                                               @RequestParam("version") String version) {
        PageInfo<ReportEntity> pageInfo =new PageInfo<>(reportService.selectAllReport(project, version, jsonObject.getInteger("pageIndex"), jsonObject.getInteger("pageSize")));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", pageInfo.getList());
        returnData.put("itemsCount", pageInfo.getTotal());
        return returnData;
    }
}
