package com.example.demo.config;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.config </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/3/24 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * spring启动以后初始化
 *如果有多个实现类，而你需要他们按一定顺序执行的话，可以在实现类上加上@Order注解。@Order(value=整数值)。SpringBoot会按照@Order中的value值从小到大依次执行
 */
@Component
@Order(value = 1)
public class ApplicationRunnerImpl implements ApplicationRunner{

    @Value("${mubanPath}")
    private String mubanPath;

    @Value("${reportPath}")
    private String reportPath;

    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${downloadPath}")
    private String downloadPath;

    @Value("${gitCodePath}")
    private String gitCodePath;

    @Value("${pretreatmentPath}")
    private String pretreatmentPath;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        File muban = new File(mubanPath);
//        // File report = new File(reportPath);
//        // File upload = new File(uploadPath);
//        File download = new File(downloadPath);
//        File gitCode = new File(gitCodePath);
//        File pretreatment = new File(pretreatmentPath);
//
//        try{
//            setPermission(muban);
//            // setPermission(report);
//            // setPermission(upload);
//            setPermission(download);
//            set(gitCode);
//            setPermission(pretreatment);
//        }catch (Exception e){
//            e.printStackTrace();
//            LOGGER.error(e.getMessage());
//        }
    }

    public void set(File file){
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true, false);
    }

    public void setPermission(File dirFile){
        if(dirFile.exists()){
            if(dirFile.isFile()){
                set(dirFile);
            }else if(dirFile.isDirectory()){
                File[] files = dirFile.listFiles();
                if(files != null && files.length > 0){
                    for(int i=0; i<files.length; i++){
                        setPermission(files[i]);
                    }
                }
                set(dirFile);
            }
        }
    }
}
