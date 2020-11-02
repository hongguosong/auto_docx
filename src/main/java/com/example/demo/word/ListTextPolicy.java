package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/6 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ListTextPolicy extends AbstractRenderPolicy<ListTextData>{
    @Override
    public void doRender(RenderContext<ListTextData> context) throws Exception {
        // anywhere
        XWPFRun where = context.getWhere();
        // anything
        if(context.getThing() != null){
            if(context.getThing().getClass().equals(ListTextData.class)){
                ListTextData thing = context.getThing();
                // do 文本
                if(thing.getCurrent() != null){
                    int current = thing.getCurrent();
                    if(thing.getTextList()!=null && thing.getTextList().size()>0 && current<thing.getTextList().size()){
                        where.setText(thing.getTextList().get(current), 0);
                        thing.setCurrent(current+1);
                    }
                }
            }
        }
    }
}
