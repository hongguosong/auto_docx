package com.example.demo.word;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.word </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/6 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.util.BytePictureUtils;
import com.example.demo.config.exception.BusinessException;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ListPicturePolicy extends AbstractRenderPolicy<ListPictureData>{
    public static int suggestFileType(String imgFile) {
        byte format;
        if (imgFile.endsWith(".emf")) {
            format = 2;
        } else if (imgFile.endsWith(".wmf")) {
            format = 3;
        } else if (imgFile.endsWith(".pict")) {
            format = 4;
        } else if (!imgFile.endsWith(".jpeg") && !imgFile.endsWith(".jpg")) {
            if (imgFile.endsWith(".png")) {
                format = 6;
            } else if (imgFile.endsWith(".dib")) {
                format = 7;
            } else if (imgFile.endsWith(".gif")) {
                format = 8;
            } else if (imgFile.endsWith(".tiff")) {
                format = 9;
            } else if (imgFile.endsWith(".eps")) {
                format = 10;
            } else if (imgFile.endsWith(".bmp")) {
                format = 11;
            } else {
                if (!imgFile.endsWith(".wpg")) {
                    throw new RenderException("Unsupported picture: " + imgFile + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
                }

                format = 12;
            }
        } else {
            format = 5;
        }

        return format;
    }
    @Override
    public void doRender(RenderContext<ListPictureData> context) throws Exception {
        int standWidth = 600;
        double standHeight = 80.0 / 89;
        // anywhere
        XWPFRun where = context.getWhere();
        // do 文本
        where.setText("", 0);
        // anything
        if(context.getThing() != null){
            if(context.getThing().getClass().equals(ListPictureData.class)){
                ListPictureData thing = context.getThing();
                if(thing.getCurrent() != null){
                    int current = thing.getCurrent();
                    if(thing.getPictureList()!=null && thing.getPictureList().size()>0 && current<thing.getPictureList().size()){
                        String path = thing.getPictureList().get(current);
                        if(path != null && path != ""){
                            String type = path.substring(path.lastIndexOf("."));
                            String name = path.substring(path.lastIndexOf(File.separator)+1);
                            try {
                                BufferedImage bufferedImage = ImageIO.read(new File(path));
                                //int width = bufferedImage.getWidth();
                                int height = bufferedImage.getHeight();
                                ByteArrayInputStream ins = new ByteArrayInputStream(BytePictureUtils.getBufferByteArray(bufferedImage));
                                where.addPicture((InputStream) ins,suggestFileType(type),name,standWidth*9525,((int) (standHeight * height))*9525);
                            }catch (IOException e){
                                throw new BusinessException("picture not found.");
                            }
                        }
                        thing.setCurrent(current+1);
                    }
                }
            }
        }
    }
}
