package com.example.demo.config.exception;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.config.exception </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/19 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class JsonResult {
    private int code;   //返回码 非0即失败
    private String msg; //消息提示
    private Map<String, Object> data; //返回的数据

    public JsonResult(){};

    public JsonResult(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static String success() {
        return success(new HashMap(0));
    }
    public static String success(String msg) {
        return JSON.toJSONString(new JsonResult(0, msg, new HashMap(0)));
    }
    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(0, "解析成功", data));
    }
    public static String failed() {
        return failed("解析失败");
    }
    public static String failed(String msg) {
        return failed(-1, msg);
    }
    public static String failed(int code, String msg) {
        return JSON.toJSONString(new JsonResult(code, msg, new HashMap(0)));
    }
}
