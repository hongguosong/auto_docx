package com.example.demo.task;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.task </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/1/13 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.config.exception.BusinessException;
import com.example.demo.dto.ReportDto;
import com.example.demo.email.service.EmailService;
import com.example.demo.service.LogService;
import com.example.demo.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class ProduceReportTask {

    @Autowired
    private ReportService reportService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LogService logService;

    private Logger logger = LoggerFactory.getLogger(ProduceReportTask.class);

    public static List<ReportDto> reportList = Collections.synchronizedList(new ArrayList<>());
    public static boolean isDo = false;

    public synchronized static void setIsDo(boolean res){
        isDo = res;
    }

    public static boolean putIfAbsent(ReportDto x) {
        synchronized (reportList) {  //获得list锁对象，其他线程将不能获得list锁来来改变list对象。
            boolean absent = !reportList.contains(x);
            if (absent)
                reportList.add(x);
            return absent;
        }
    }
    public static boolean removeIfAbsent(ReportDto x) {
        synchronized (reportList) {  //获得list锁对象，其他线程将不能获得list锁来来改变list对象。
            boolean absent = reportList.contains(x);
            if (absent)
                reportList.remove(x);
            return absent;
        }
    }

    @Scheduled(fixedRate=2000)
    private void configureTasks() {
//        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        if(isDo == false) {
            setIsDo(true);
//            try {
//                Thread.sleep(10000);
//            }catch (Exception e){
//
//            }
            if(reportList != null && reportList.size() > 0){
                ReportDto report = reportList.get(0);
                try{
                    if(report.getFileType() == 1){
                        reportService.produceReport(report.getProjectId(), report.getProjectVersion(), report);
                    }else{
                        reportService.produceReport2(report.getProjectId(), report.getProjectVersion(), report);
                    }
                    reportService.insert(report);


//                    String fileUrl = report.getUrl();
//                    if(fileUrl.contains("\\")){
//                        fileUrl = report.getUrl().replace(File.separator, "%5C");
//                    } else if(fileUrl.contains("/")){
//                        fileUrl = report.getUrl().replace(File.separator, "%2F");
//                    }
                    String url = report.getEmailLink()+"fileName="+ URLEncoder.encode(report.getUrl(),"UTF-8") + "&reportName=" + report.getName();
                    try {
                        emailService.sendEmailBack(0, url, report);
                    }catch (Exception e){
                        throw new BusinessException("通知邮件未发送成功,请确认邮件地址及网关信息.");
                    }

                    String log = "生成版本为“" + report.getProjectVersion() + "”的“" + report
                            .getProjectName()+ "”项目的“" + report.getName() + "”报告";
                    Timestamp createTime = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    logService.insertLog(log, report.getCreatePerson(), createTime);

                } catch (Exception e){
                    try {
                        logger.error(e.getMessage());
                        emailService.sendEmailBack(1, e.getMessage(), report);
                    }catch (Exception ee){
                        setIsDo(false);
                    }
                } catch (OutOfMemoryError e){
                    logger.error(e.getMessage());
                    try {
                        emailService.sendEmailBack(1, "java.lang.OutOfMemoryError: " + e.getMessage(), report);
                    }catch (Exception ee){
                        setIsDo(false);
                    }
                } finally {
                    removeIfAbsent(report);
//                    System.err.println("生成报告时间: " + LocalDateTime.now());
                    setIsDo(false);
                }
            }
            setIsDo(false);
        }
    }
}
