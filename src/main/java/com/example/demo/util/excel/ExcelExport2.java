package com.example.demo.util.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 导出Excel公共方法
 * @version 1.0
 *
 * @author wangcp
 *
 */
public class ExcelExport2{

    private static Logger log = LoggerFactory.getLogger(ExcelExport2.class);
    //显示的导出表的标题
    private String title;
    //导出表的列名
    private JSONArray rowName ;

    private JSONArray header;

    private JSONArray dataList;

    HttpServletResponse response;

    //构造方法，传入要导出的数据
    public ExcelExport2(String title, JSONArray rowName, JSONArray header, JSONArray dataList){
        this.dataList = dataList;
        this.rowName = rowName;
        this.header = header;
        this.title = title;
    }

    public static void out(HSSFWorkbook wb, HttpServletResponse response){
        try{
            if(wb != null){
                String headStr = "attachment; filename=\"" + "function" + ".xls\"";
                response.setContentType("APPLICATION/OCTET-STREAM");//返回格式为流
                response.setHeader("Content-Disposition", headStr);
                // response.setHeader("Content-type","application/octet-stream");
                response.setHeader("Content-type","application/vnd.ms-excel;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                //response.setHeader("Content-Length", String.valueOf(file.length()));
                response.setStatus(HttpStatus.OK.value());
                OutputStream out = response.getOutputStream();
                wb.write(out);
                //wb.write(new FileOutputStream(new File("D://a.xls")));
                wb.close();
                out.close();
            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    /*
     * 导出数据
     * */
    public HSSFWorkbook export(){
        try{
            HSSFWorkbook workbook = new HSSFWorkbook();						// 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title);		 			// 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook);					//单元格样式对象

            CellRangeAddress cra = new CellRangeAddress(0, 1, 0, (rowName.size()-1));
            sheet.addMergedRegion(cra);
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);
            setCellMegerStyle(cra, sheet);

            // 定义所需列数
            int columnNum = rowName.size();
            HSSFRow rowRowName = sheet.createRow(2);				// 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for(int n=0;n<columnNum;n++){
                HSSFCell cellRowName = rowRowName.createCell(n);				//创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING);				//设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(rowName.get(n).toString());
                cellRowName.setCellValue(text);									//设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle);						//设置列头单元格样式
            }

            //将查询出的数据设置到sheet对应的单元格中
            for(int i=0;i<dataList.size();i++){

                JSONObject obj = dataList.getJSONObject(i);//遍历每个对象
                HSSFRow row = sheet.createRow(i+3);//创建所需的行数

                for(int j=0; j<header.size(); j++){
                    HSSFCell cell = null;   //设置单元格的数据类型
                    cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue(obj.getString(header.getString(j)));
                    cell.setCellStyle(style);									//设置单元格样式
                }
            }
            //让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    //当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
                if(colNum == 0){
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
                }else{
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                }
            }

            return workbook;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setCellMegerStyle(CellRangeAddress cra, HSSFSheet sheet){
        RegionUtil.setBorderBottom(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN,cra,sheet);
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)11);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);

        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}