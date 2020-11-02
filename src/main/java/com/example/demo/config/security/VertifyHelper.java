package com.example.demo.config.security;/*
 * <p>项目名称: ${project_name} </p>
 * <p>文件名称: ${file_name} </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: ${date} </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">${user}</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

public class VertifyHelper {

    public static VertifyHelper instance = new VertifyHelper();

    public static synchronized VertifyHelper getInstance () {
        return instance;
    }

    private LicenseVertify vlicense;

    private VertifyHelper() {
        vlicense=new LicenseVertify("noryar"); // 项目唯一识别码，对应生成配置文件的subject
        vlicense.install(System.getProperty("user.dir"));
    }

    public boolean vertify () {
        if(vlicense.vertify() == 0){
            return true;
        } else  {
            return false;
        }
    }
}
