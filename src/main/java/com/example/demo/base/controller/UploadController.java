package com.example.demo.base.controller;

import com.example.demo.base.service.UploadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(value = "/file")
public class UploadController {

    Logger logger = LogManager.getLogger(UploadController.class.getName());

//    @Value("${sys.path.base}")
//    private String bathPath;

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("type") String type,
                         @RequestParam("picName") String picName,
                         @RequestParam("picDescription") String picDescription,
                         @RequestParam("picFile") MultipartFile picFile) throws IOException {
        try {
            String bathPath = "D:/IdeaProjects/auto_docx_upload";
            uploadService.addFiles(type, picName, picDescription, picFile, bathPath);
        }catch (Exception e){
            logger.error(e);
            return "上传失败";
        }
        return "上传成功";
    }

    @RequestMapping(value = "/download")
    public String download(){

        return "下载成功";
    }
}
