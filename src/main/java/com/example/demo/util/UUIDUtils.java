package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/17 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import java.util.UUID;

public class UUIDUtils {

    /**
     *32位默认长度的uuid
     * (获取32位uuid)
     *
     * @return
     */
    public static  String getUUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     *
     * (获取指定长度uuid)
     *
     * @return
     */
    public static  String getUUID(int len)
    {
        if(0 >= len)
        {
            return null;
        }

        String uuid = getUUID()+getUUID();
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < len; i++)
        {
            str.append(uuid.charAt(i));
        }

        return str.toString();
    }
}
