package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/28 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;

import java.util.List;

public class ProblemTablePolicy extends DynamicTableRenderPolicy{

    //问题单动态属性填充行数
    int propertyStartRow = 12;

    public void setAD(XWPFParagraph p1){
        CTP ctp = p1.getCTP();
        CTPPr CTPpr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTJc jc = CTPpr.isSetJc() ? CTPpr.getJc() : CTPpr.addNewJc();
        CTString style = CTPpr.getPStyle() != null ? CTPpr.getPStyle() : CTPpr.addNewPStyle();
        style.setVal("ad");
    }

    @Override
    public void render(XWPFTable table, Object data) {
        if(null == data){
            return;
        }
        ProblemPropertyData propertyData = (ProblemPropertyData) data;
        List<RowRenderData> properties = propertyData.getProperties();
        String style = table.getRow(0).getCell(0).getParagraphs().get(0).getStyle();
        table.removeRow(propertyStartRow);
        if(properties != null){
            for(int i=0; i<properties.size(); i++){
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(propertyStartRow);
                for (int j = 0; j < 7; j++) {
                    XWPFTableCell cell = insertNewTableRow.createCell();
                }
                // 合并单元格
                TableTools.mergeCellsHorizonal(table, propertyStartRow, 1, 6);
                // 渲染问题单动态属性
                MiniTableRenderPolicy.Helper.renderRow(table, propertyStartRow, properties.get(i));
                //设置左边的单元格居中对齐
//                XWPFParagraph p1 = table.getRow(propertyStartRow).getCell(0).getParagraphs();
//                XWPFRun run1 = p1.createRun();
//                run1.setText("XXX", 0);
//                run1.setStyle("ad");
                XWPFParagraph p1 = table.getRow(propertyStartRow).getCell(0).getParagraphs().get(0);
                p1.setAlignment(ParagraphAlignment.CENTER);
                p1.setStyle(style);

                XWPFParagraph p2 = table.getRow(propertyStartRow).getCell(1).getParagraphs().get(0);
                p2.setAlignment(ParagraphAlignment.LEFT);
                p2.setStyle(style);
//                table.getRow(propertyStartRow).getCell(0).getParagraphs().get(0).setStyle("ae");
//                table.getRow(propertyStartRow).getCell(1).getParagraphs().get(0).setStyle("ae");
//                XWPFParagraph p1 = table.getRow(propertyStartRow).getCell(0).getParagraphs().get(0);
//                p1.setStyle("ae");
//                XWPFRun run = p1.createRun();
//                run.setText(properties.get(i).getCellDatas().get(0).getRenderData().getText(),0);
//                //run.setStyle("ad");
//                //setAD(p1);
//
//                XWPFParagraph p2 = table.getRow(propertyStartRow).getCell(1).getParagraphs().get(0);
//                p2.setStyle("ae");
//                XWPFRun run2 = p2.createRun();
//                run2.setText(properties.get(i).getCellDatas().get(1).getRenderData().getText(),0);
//                //run2.setStyle("ad");
//                //setAD(p2);
            }
        }
    }
}
