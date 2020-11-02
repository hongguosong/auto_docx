package com.example.demo.email.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Liu
 * @create 2019-11-08
 */
@Controller
@RequestMapping(value = "email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Value("${attachmentPath}")
    private String filePath;

    @RequestMapping(value = "getUserList")
    @ResponseBody
    public JSONObject getUserList (@RequestParam(value = "pageIndex",defaultValue = "0",required = false) Integer pageIndex,
                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize) {

        return emailService.getUserList(pageIndex,pageSize);
    }

    @RequestMapping(value = "sendEmail")
    @ResponseBody
    public String sendEmail (@RequestParam(value = "data") String data) {

        emailService.sendEmail(data);
        return "success";
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    public String fileUpload (@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        System.out.println("文件名："+fileName);
        //获取文件大小
        long fileSize = file.getSize();
        System.out.println("文件大小："+fileSize+" 字节");
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println("文件类型："+suffixName);
        // 文件上传后的路径
//        String filePath = "C://auto_docx//attachment//";
        //绝对路径
        String absolutePath = filePath+fileName;
        System.out.println("文件保存路径为："+absolutePath);
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return absolutePath;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Boolean emailTest (@RequestParam(value = "data") String file) {

        System.out.println(file);
        return true;
    }
}
