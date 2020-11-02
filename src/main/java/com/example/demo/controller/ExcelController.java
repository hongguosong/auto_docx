package com.example.demo.controller;/*
 * <p>项目名称: SpringBoot-Shiro-Vue </p>
 * <p>包名称: com.heeexy.example.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/5/7 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.dao.ProjectProcedureDao;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.service.ProcedureService;
import com.example.demo.util.excel.ExcelExport;
import com.example.demo.util.excel.ExcelExport2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ProcedureService procedureService;

    private Logger log = LoggerFactory.getLogger(ExcelController.class);

    @RequestMapping(value = "exportGrid", method = RequestMethod.POST)
    @ResponseBody
    public void downLoadRpt(@RequestBody JSONObject obj,
                              HttpServletRequest request,
                              HttpServletResponse response){
        // String tempPath = request.getSession().getServletContext().getRealPath("/");

        JSONArray jsonColumns = obj.getJSONArray("columns");
        JSONArray jsonData = obj.getJSONArray("data");

        try{
            ExcelExport export = new ExcelExport(100);
            HSSFWorkbook wb = export.getWorkbook();

            //设置单元格样式
            CellStyle cellStyleTitle = wb.createCellStyle();
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fontTitle = wb.createFont();
            fontTitle.setFontName("宋体");
            fontTitle.setBold(true);
            fontTitle.setItalic(false);
            fontTitle.setFontHeight((short) 240);
            cellStyleTitle.setFont(fontTitle);

            CellStyle cellStyleContent = wb.createCellStyle();
            cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
            cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fontContent = wb.createFont();
            fontContent.setFontName("宋体");
            fontContent.setBold(true);
            fontContent.setItalic(false);
            fontContent.setFontHeight((short) 200);
            cellStyleTitle.setFont(fontContent);

            List<Map<String, Object>> list1 = new ArrayList<>();
            List<JSONObject> list2 = new ArrayList<>();
            List<JSONObject> bottomColList = new ArrayList<>();

            //解析表头数据
            for(int i=0; i<jsonColumns.size();i++){
                JSONObject jsonObject = jsonColumns.getJSONObject(i);
                Map<String, Object> resultMap = new HashMap<>();
                JSONArray chidren = jsonObject.getJSONArray("columns");
                if(chidren == null){
                    resultMap.put("rowSpan",jsonObject.getInteger("rowSpan"));
                    resultMap.put("colSpan",1);
                    bottomColList.add(jsonObject);
                }else{
                    resultMap.put("colSpan",jsonObject.getInteger("colSpan"));
                    resultMap.put("rowSpan",1);
                    for(int j=0; j<chidren.size(); j++){
                        list2.add(chidren.getJSONObject(j));
                        bottomColList.add(chidren.getJSONObject(j));
                    }
                }
                String cellValue = jsonObject.getString("header").replace("\n", "").trim();
                resultMap.put("header",cellValue);
                list1.add(resultMap);
            }

            //填充表头
            export.createNextRow();
            list1.forEach(map -> {
                Cell cell = export.createCell((int) map.get("colSpan"), (int) map.get("rowSpan"));
                cell.setCellValue((String) map.get("header"));
                cell.setCellStyle(cellStyleTitle);
            });
            export.createNextRow();
            for(JSONObject aList2: list2){
                Cell cell = export.createCell();
                cell.setCellValue(aList2.getString("header").replace("\n", "").trim());
                cell.setCellStyle(cellStyleTitle);
            }

            //填充数据
            for(int j=0; j<jsonData.size();j++){
                JSONObject jsonRow = jsonData.getJSONObject(j);
                export.createNextRow();
                for(int i=0; i<bottomColList.size(); i++){
                    JSONObject jsonObject = bottomColList.get(i);
                    Cell cell = export.createCell();
                    String tmpVal = jsonRow.getString(jsonObject.getString("field"));
                    cell.setCellValue(tmpVal);
                    cell.setCellStyle(cellStyleContent);
                    export.getSheet().setColumnWidth(i, 18* 256);
                }
            }

//            File file = new File(tempPath, UUID.randomUUID().toString()+".xls");
//            if(file.exists()){
//                file.createNewFile();
//            }
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            wb.write(fileOutputStream);
//            fileOutputStream.close();
//            wb.close();
//
//            String fileDisplay = file.getAbsolutePath();
//            fileDisplay = fileDisplay.substring(fileDisplay.lastIndexOf(File.separator)+1);
//            fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
//
//            InputStream is = new FileInputStream(file);
//            response.setHeader("Content-Disposition","attachment;filename="+fileDisplay);
//            response.setHeader("Content-type","application/octet-stream");
//            response.setHeader("Content-Length", String.valueOf(file.length()));
//            response.setStatus(HttpStatus.OK.value());
//
//            IOUtils.copy(is, response.getOutputStream());
//            response.flushBuffer();
//            file.delete();

            ExcelExport2.out(wb, response);

        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    @RequestMapping(value = "exportGrid2", method = RequestMethod.POST)
    @ResponseBody
    public void downLoadRpt2(@RequestBody JSONObject obj,
                              HttpServletResponse response) {
        String title = obj.getString("title");
        JSONArray jsonColumns = obj.getJSONArray("columns");
        JSONArray header = obj.getJSONArray("header");
        JSONArray jsonData = obj.getJSONArray("data");

        ExcelExport2 excelExport2 = new ExcelExport2(title,jsonColumns,header, jsonData);
        HSSFWorkbook wb = excelExport2.export();
        ExcelExport2.out(wb, response);
    }

    @RequestMapping(value = "exportGridAll")
    @ResponseBody
    public void downLoadRpt3(@RequestParam(name = "projectId") Integer projectId,
                                @RequestParam(name = "version") String version,
                                HttpServletResponse response) {
        String title = "FUNCTION";
        JSONArray jsonColumns = JSONArray.parseArray("[\"id\",\"name\",\"longName\",\"fullName\",\"type\",\"version\",\"hashValue\",\"returnType\",\"lineCount\",\"creatTime\",\"updateTime\"]");
        JSONArray header = JSONArray.parseArray("[\"id\",\"name\",\"longName\",\"fullName\",\"type\",\"version\",\"hashValue\",\"returnType\",\"lineCount\",\"creatTime\",\"updateTime\"]");
        List<ProcedureDto> list = procedureService.selectFunctionsByProjectId(projectId,version);
        JSONArray jsonData = JSONArray.parseArray(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));

        ExcelExport2 excelExport2 = new ExcelExport2(title,jsonColumns,header, jsonData);
        HSSFWorkbook wb = excelExport2.export();
        ExcelExport2.out(wb, response);
    }
}
