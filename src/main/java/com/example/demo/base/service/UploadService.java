package com.example.demo.base.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {

    public void addFiles(String type, String picName, String picDescription, MultipartFile picFile, String bathPath) throws IOException {
        //创建文件夹
        String path = File.separator + type + File.separator + picName + File.separator;
        File dir = new File(bathPath, path);
        if (!dir.exists()){
            dir.mkdirs();
        }

        //保存到本地文件
        File file = new File(dir, picFile.getOriginalFilename());
        if (file.exists()){
            file.delete();
        }
        picFile.transferTo(file);

        //保存到数据库
    }
}
