package com.example.demo.word;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/30 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

public class TocPolicy implements RenderPolicy{

    @Override
    public void render(ElementTemplate elementTemplate, Object o, XWPFTemplate xwpfTemplate) {
        XWPFRun run = ((RunTemplate) elementTemplate).getRun();
        // String thing = String.valueOf(data);
        String thing = "";
        run.setText(thing, 0);


//        CTSdtBlock block = run.getDocument().getDocument().getBody().addNewSdt();
//        //CTSdtBlock block = xwpfTemplate.getXWPFDocument().getDocument().getBody().addNewSdt();
//
//        TOC toc = new TOC(block);
//        List<XWPFParagraph> paragraphs = xwpfTemplate.getXWPFDocument().getParagraphs();
//        for (XWPFParagraph par : paragraphs) {
//            String parStyle = par.getStyle();
//            if (parStyle != null && (parStyle.startsWith("1") || parStyle.startsWith("2") || parStyle.startsWith("3"))) {
//
//                //获取书签，书签的对应关系很重要，关系到目录能否正常跳转
//                List<CTBookmark> bookmarkList = par.getCTP().getBookmarkStartList();
//                try {
//                    int level = Integer.parseInt(parStyle);
//
//                    //添加标题
//                    if(bookmarkList.size()>0){
//                        toc.addRow(level, par.getText(), 1, bookmarkList.get(0).getName());
//                    }else{
//                        toc.addRow(level, par.getText(), 1, "12344455");
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        XWPFParagraph paragraph = (XWPFParagraph) run.getParent();
        CTSimpleField ctSimpleField = paragraph.getCTP().addNewFldSimple();
        ctSimpleField.setInstr("TOC \\o \"1-3\" \\h \\z \\u");
        ctSimpleField.setDirty(STOnOff.ON);
        ctSimpleField.addNewR().addNewT().setStringValue("<<fieldName>>");
    }
}
