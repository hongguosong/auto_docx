package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/1/2 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String getFormaterDate(Date date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
