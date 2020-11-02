package com.example.demo.email.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.EmailDao;
import com.example.demo.dto.ReportDto;
import com.example.demo.entity.EmailEntity;
import com.example.demo.entity.ReportEntity;
import com.example.demo.security.dao.UserRepository;
import com.example.demo.security.entity.User;
import com.example.demo.service.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Liu
 * @create 2019-11-08
 */
@Service
public class EmailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailDao emailDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CurrentUserService currentUserService;

    @Value("${spring.mail.username}")
    private String from;

    private Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public JSONObject getUserList (Integer pageIndex, Integer pageSize) {

        Page<User> users = userRepository.findAll(PageRequest.of(pageIndex,pageSize));
        JSONObject object = new JSONObject();
        List<User> list = users.getContent();
        long itemsCount = users.getTotalElements();
        object.put("data",list);
        object.put("itemsCount",itemsCount);
        return object;
    }

    public void sendEmail (String data) {

        JSONObject object = JSONObject.parseObject(data);

        String to = object.getString("address");
        String subject = object.getString("subject");
        String fileUrls = object.getString("filePaths");
        List<String> filePaths = JSON.parseArray(fileUrls,String.class);
        String content = object.getString("content");
        Integer state = object.getInteger("state");
        Integer userId = object.getInteger("userId");

        /*EmailEntity emailEntity = new EmailEntity();
        emailEntity.setId(UUID.randomUUID().toString().replaceAll("-",""));
        emailEntity.setEmailFrom(from);
        emailEntity.setEmailTo(to);
        emailEntity.setEmailSubject(subject);
        emailEntity.setCreateTime(new Date());
        emailEntity.setEmailAttachment(fileUrls);
        emailEntity.setEmailContent(content);
        emailEntity.setState(state);
        emailEntity.setUserId(userId);*/

        sendHtmlMail(from,to,subject,content,filePaths);
        /*saveEmail(emailEntity);*/
    }

    public void sendEmailBack (Integer code, String ctx, ReportDto report) {
        if(code == 0){
            String to = report.getTo();
            String subject = "文档生成完成通知";
            List<String> filePaths = new ArrayList<>();
            String content = "<p>文档 "+"<a href='"+ctx+"'>"+report.getName()+"</a>生成完成.</p> ";
            sendHtmlMail(from,to,subject,content,filePaths);
        }else{
            String to = report.getTo();
            String subject = "文档生成错误通知";
            List<String> filePaths = new ArrayList<>();
            String content = "<p style='color:red'>文档生成错误： " + ctx + "</p> ";
            sendHtmlMail(from,to,subject,content,filePaths);
        }
    }

    public void sendHtmlMail(String from,String to,String subject,String content,List<String> filePaths) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        FileSystemResource fileResource;
        try {
            helper=new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(from);
            helper.setTo(to.split(";"));
            helper.setSubject(subject);
            for (String filePath: filePaths) {
                fileResource = new FileSystemResource(new File(filePath));
                helper.addAttachment(fileResource.getFilename(),fileResource);
            }
            helper.setText(content,true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void saveEmail (EmailEntity email) {
        emailDao.insert(email);
    }
}
