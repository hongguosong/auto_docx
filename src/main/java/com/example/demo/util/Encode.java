package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/4/24 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Encode {
    private static Logger LOGGER = LoggerFactory.getLogger(Encode.class);
    public static String encode(String str){
        String result = "";
        try{
            result = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        }catch (UnsupportedEncodingException e){
            LOGGER.error(str+"编码失败.");
            LOGGER.error(e.getMessage());
        }
        return result;
    }
    public static String decode(String str){
        String result = "";
        try{
            byte[] dc = Base64.getMimeDecoder().decode(str);
            result = new String(dc, "utf-8");
        }catch (UnsupportedEncodingException e){
            LOGGER.error(str+"解码失败.");
            LOGGER.error(e.getMessage());
        }
        return result;
    }
}
