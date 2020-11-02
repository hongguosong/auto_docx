package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/5/13 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtils {
    public static Integer getId(String str){
        Pattern p=Pattern.compile("'(.*?)'");
        Matcher m=p.matcher(str);
        int i=0;
        while(m.find())
        {
            String s = m.group();
            if(s.contains("-")){
                s = s.replace("'", "");
                String[] items = s.split("-");
                if(items.length == 3){
                    return Integer.valueOf(items[2]);
                }
            }
        }
        return -1;
    }
}
