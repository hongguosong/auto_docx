package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/27 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.data.RowRenderData;

import java.util.List;

public class TableData {
    RowRenderData header;
    List<RowRenderData> tableDatas;

    public RowRenderData getHeader() {
        return header;
    }

    public void setHeader(RowRenderData header) {
        this.header = header;
    }

    public List<RowRenderData> getTableDatas() {
        return tableDatas;
    }

    public void setTableDatas(List<RowRenderData> tableDatas) {
        this.tableDatas = tableDatas;
    }
}
