package com.example.demo.service;/*
 * <p>项目名称: demo </p>
 * <p>包名称: com.example.demo.Service </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/3 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.fastjson.JSONArray;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.util.BytePictureUtils;
import com.example.demo.config.exception.BusinessException;
import com.example.demo.dao.*;
import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.*;
import com.example.demo.util.DateUtil;
import com.example.demo.word.*;
import com.github.pagehelper.PageHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.hibernate.annotations.Source;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.rmi.CORBA.Util;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class ReportService {
    @Value("${mubanPath}")
    private String mubanPath;

    @Value("${reportPath}")
    private String reportPath;

    @Value("${uploadPath}")
    private String uploadPath;

    @Resource
    private ReportDao reportDao;

    @Resource
    private PictureFileDao pictureFileDao;

    @Resource
    private CoverageFileDao coverageFileDao;

    @Resource
    private TcfFileDao tcfFileDao;

    @Resource
    private WordMubanDao wordMubanDao;

    @Resource
    private ProjectDao projectDao;

    @Resource
    private ProblemDao problemDao;

    @Resource
    private ProcedureDao procedureDao;

    @Resource
    private FunDescDao funDescDao;

    @Autowired
    private ProcedureService procedureService;

//    public void checkReport(Integer project, String version) throws IOException{
//        ReportEntity report = new ReportEntity();
//        report.setFileType(1);
//        WordData wordData = new WordData();
//        StringBuilder error = new StringBuilder();
//        prepareReport(project, version, report, wordData, getXinhao(project), error);
//
//        ReportEntity report2 = new ReportEntity();
//        report2.setFileType(2);
//        DescData descData = new DescData();
//        prepareReport2(project, version, report2, descData, getXinhao(project));
//    }

    public XWPFTemplate prepareReport(Integer project, String version, ReportEntity report, WordData data, String xinhao, StringBuilder error, List<Map<String,String>> mubanFunctionList) throws IOException{
        //--------------所有文档正文都不能有首行缩进，不然表格就有缩进------------------------
        //-----------------------------------------------------------------------------
        //word复选框，有钩☑，无钩口, 方框字号9，打钩的10一样大
        //---------------JVM内存要分配大一点，否则报内存溢出--------------------------------------
        // -Xms128m -Xmx1548m
        //整个模板数据
        //WordData data = new WordData();
        ZipSecureFile.setMinInflateRatio(-1.0d);

        //渲染输出,可以用bind代替customPolicy
        Configure config = Configure.newBuilder().customPolicy("toc", new TocPolicy())
                .customPolicy("problem_property", new ProblemTablePolicy())
//                .customPolicy("function_name",  new ListTextPolicy())
                .customPolicy("testcase",  new ListTextPolicy())
                .customPolicy("input",  new ListTextPolicy())
                .customPolicy("output",  new ListTextPolicy())
                .customPolicy("statement",  new ListTextPolicy())
                .customPolicy("branch",  new ListTextPolicy())
//                .customPolicy("testcase_pic",  new ListPicturePolicy())
//                .customPolicy("statement_pic",  new ListPicturePolicy())
//                .customPolicy("branch_pic",  new ListPicturePolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(getWordMubanUrl(project, version, report.getFileType()), config);
        List<ProcedureDto> procedureDtoList = procedureDao.selectFunctionsByProjectId(project,version);
        getMubanFunctionList(template.getXWPFDocument(), report.getFileType(), mubanFunctionList, procedureDtoList);

        //        //分析结果
//        List<SegmentResultData> segmentsResultDataList = getSegmentResultDataList();
//        DocxRenderData segmentResult = new DocxRenderData(new File(mubanPath+File.separator+"segmentResult.docx"), segmentsResultDataList );
//        data.setSegmentResult(segmentResult);

//        String xinhao = getXinhao(project);
        //tcf_function table
        data.setTcfFunction(getTcfTable(mubanFunctionList, project, version, xinhao, error));

        List<ProcedureDto> staticProcedureList = getStaticFunctionList(project,version);
        //static_function table
        data.setStaticFunction(getStaticTable(staticProcedureList, project, version));

//        //截图文件
//        List<SegmentPictureData> segmentPictureDataList  = getSegmentPictureDataList(mubanFunctionList, project, version, error);
//        DocxRenderData segmentPicture = new DocxRenderData(new File(mubanPath+File.separator+"segmentPicture.docx"), segmentPictureDataList );
//        data.setSegmentPicture(segmentPicture);

        //问题单
        List<SegmentProblemData> segmentProblemDataList = getSegmentProblemList(mubanFunctionList,staticProcedureList,project, version, xinhao);
        if(segmentProblemDataList.size()>0){
            DocxRenderData segmentProblem = new DocxRenderData(new File(mubanPath+File.separator+"segmentProblem.docx"), segmentProblemDataList);
            data.setSegmentProblem(segmentProblem);
        }

        data.setTestcase(getTestCase(mubanFunctionList, project, version, xinhao, error));
        ListTextData inputData = new ListTextData();
        ListTextData outputData = new ListTextData();
        ListTextData descData = new ListTextData();
        getInputOutPut(mubanFunctionList, inputData, outputData, descData, project, version, error,1);
        data.setInput(inputData);
        data.setOutput(outputData);

//        data.setFunctionName(getFunctionName(mubanFunctionList));
        data.setStatement(getStatementCoverage(mubanFunctionList, project, version, error));
        data.setBranch(getBranchCoverage(mubanFunctionList,project,version, error));
//        data.setTestcasePic(getPic(mubanFunctionList,CoverageType.TESTCASE,project,version));
//        data.setStatementPic(getPic(mubanFunctionList,CoverageType.STATEMENT,project,version));
//        data.setBranchPic(getPic(mubanFunctionList,CoverageType.BRANCH,project,version));

        return template;
    }

    /**
     * 生成测试报告
     * @param project
     * @param version
     * @param report
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void produceReport(Integer project, String version, ReportEntity report) throws IOException, InvalidFormatException {

        String xinhao = getXinhao(project);
        WordData data = new WordData();
        StringBuilder error = new StringBuilder();
        List<Map<String, String>> mubanFunctionList = new ArrayList<>();
        XWPFTemplate template = prepareReport(project, version, report, data, xinhao, error, mubanFunctionList);

        template.render(data);
        renderPic(template.getXWPFDocument(), mubanFunctionList, project, version, error);
        String filePath = reportPath+File.separator+UUID.randomUUID()+File.separator;
        Calendar now = Calendar.getInstance();
        Date nowDate = now.getTime();
        report.setCreateTime(nowDate);
        String reportName = xinhao+"_REPORT_"+DateUtil.getFormaterDate(nowDate,"yyyyMMdd_HHmmss")+".docx";
        String fileName = filePath + reportName;
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
//        FileOutputStream out = new FileOutputStream(file);
//        template.write(out);
        File reportFile = new File(fileName);
        OutputStream out = new FileOutputStream(reportFile);

//        template.writeToFile(fileName);
        template.write(out);
        out.flush();
        out.close();
        template.close();
        File reportPathFile = new File(reportPath);
        report.setUrl(reportFile.getAbsolutePath().replace(reportPathFile.getAbsolutePath(), ""));
        report.setName(reportName);

        String errorName = "Log_"+DateUtil.getFormaterDate(nowDate,"yyyyMMdd_HHmmss")+".txt";
        File errorFile = new File(filePath + errorName);
        writeTxt(error,errorFile,reportPathFile,report);
    }

    public void writeTxt(StringBuilder error, File errorFile, File reportPathFile, ReportEntity report) throws IOException{
        if(error.length() > 0){
            BufferedWriter bw = new BufferedWriter(new FileWriter(errorFile));
            bw.write(error.toString());
            bw.flush();
            bw.close();
            report.setAddress(errorFile.getAbsolutePath().replace(reportPathFile.getAbsolutePath(), ""));
        }else{
            report.setAddress("");
        }
    }

    public XWPFTemplate prepareReport2(Integer project, String version, ReportEntity report, DescData data, String xinhao, StringBuilder error) throws IOException{

        ZipSecureFile.setMinInflateRatio(-1.0d);

        //渲染输出,可以用bind代替customPolicy
        Configure config = Configure.newBuilder()
                .customPolicy("description",  new ListTextPolicy())
                .customPolicy("testcase",  new ListTextPolicy())
                .customPolicy("input",  new ListTextPolicy())
                .customPolicy("output",  new ListTextPolicy())
                .customPolicy("statement",  new ListTextPolicy())
                .customPolicy("branch",  new ListTextPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(getWordMubanUrl(project, version, report.getFileType()), config);
        List<Map<String, String>> mubanFunctionList = new ArrayList<>();
        List<ProcedureDto> procedureDtoList = procedureDao.selectFunctionsByProjectId(project,version);
        getMubanFunctionList(template.getXWPFDocument(), report.getFileType(), mubanFunctionList, procedureDtoList);

        //function table
        data.setFunction(getAllFunctionTable(mubanFunctionList, project, version, xinhao));

        data.setTestcase(getTestCase2(mubanFunctionList, project, version, xinhao, error));

        ListTextData inputData = new ListTextData();
        ListTextData outputData = new ListTextData();
        ListTextData descData = new ListTextData();
        getInputOutPut(mubanFunctionList, inputData, outputData, descData, project, version, error,2);
        data.setInput(inputData);
        data.setOutput(outputData);
        data.setDescription(descData);
        data.setStatement(getStatementCoverage(mubanFunctionList, project, version, error));
        data.setBranch(getBranchCoverage(mubanFunctionList,project,version, error));

        return template;
    }

    /**
     * 生成说明报告
     * @param project
     * @param version
     * @param report
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void produceReport2(Integer project, String version, ReportEntity report) throws IOException, InvalidFormatException {

        String xinhao = getXinhao(project);
        DescData data = new DescData();
        StringBuilder error = new StringBuilder();
        XWPFTemplate template = prepareReport2(project, version, report, data, xinhao, error);
        template.render(data);
        String filePath = reportPath+File.separator+UUID.randomUUID()+File.separator;
        Calendar now = Calendar.getInstance();
        Date nowDate = now.getTime();
        report.setCreateTime(nowDate);
        String reportName = xinhao+"_DESCRIPTION_"+DateUtil.getFormaterDate(nowDate,"yyyyMMdd_HHmmss")+".docx";
        String fileName = filePath + reportName;
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }

//        FileOutputStream out = new FileOutputStream(file);
//        template.write(out);
        File reportFile = new File(fileName);
        OutputStream out = new FileOutputStream(reportFile);

//        template.writeToFile(fileName);
        template.write(out);
        out.flush();
        out.close();
        template.close();
        File reportPathFile = new File(reportPath);
        report.setUrl(reportFile.getAbsolutePath().replace(reportPathFile.getAbsolutePath(), ""));
        report.setName(reportName);

        String errorName = "Log_"+DateUtil.getFormaterDate(nowDate,"yyyyMMdd_HHmmss")+".txt";
        File errorFile = new File(filePath + errorName);
        writeTxt(error,errorFile,reportPathFile,report);
    }

    public void generateTOC(XWPFDocument document) throws InvalidFormatException, FileNotFoundException, IOException {
        String findText = "toc";
        String replaceText = "";
        for (XWPFParagraph p : document.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                int pos = r.getTextPosition();
                String text = r.getText(pos);
                System.out.println(text);
                if (text != null && text.contains(findText)) {
                    text = text.replace(findText, replaceText);
                    r.setText(text, 0);
                    addField(p, "TOC \\o \"1-3\" \\h \\z \\u");
//                    addField(p, "TOC \\h");
                    break;
                }
            }
        }
    }

    private void addField(XWPFParagraph paragraph, String fieldName) {
        CTSimpleField ctSimpleField = paragraph.getCTP().addNewFldSimple();
        ctSimpleField.setInstr(fieldName);
        ctSimpleField.setDirty(STOnOff.TRUE);
        ctSimpleField.addNewR().addNewT().setStringValue("<<fieldName>>");
    }

    public List<SegmentResultData> getSegmentResultDataList() {
        //动态分析结果
        List<SegmentResultData> segmentsResultDataList = new ArrayList<>();
        for(int i=1; i<3; i++){
            SegmentResultData s1 = new SegmentResultData();
            if(i==1){
                s1.setFunction("ASP.READ");
            } else if(i==2){
                s1.setFunction("ASP.WRITE");
            } else if(i==3){
                s1.setFunction("ASP.INIT");
            }
            Random random = new Random();
            //s1.setFunction("☑口□□");
            s1.setTestcase("SDFTK_U"+i);
            s1.setInput("输入测试说明"+i);
            s1.setOutput("输出测试说明"+i);
            s1.setBranch(random.nextInt(100)+"%");
            s1.setStatement(random.nextInt(100)+"%");
            segmentsResultDataList.add(s1);
        }

        return segmentsResultDataList;
    }

    public String getXinhao(Integer project){
        ProjectEntity projectEntity = projectDao.selectByPrimaryKey(project);
        return projectEntity.getName().toUpperCase();
    }

    /**
     * 按给定的长度处插入换行
     * @param source
     * @param length
     * @return
     */
    public String lineBreak(String source, int length){
        if(source.length() <= length){
            return source;
        }
        StringBuffer str = new StringBuffer(source);
        String result = "";
        int start = 0;
        int end = start+length;

        while(true){
            if(start>=str.length()) {
                result = result.substring(0,result.length()-1);
                return result;
            }
            result = result+str.substring(start, end)+"\n";
            start = end;
            end = end+length;
            if(end>=str.length()){
                end = str.length();
            }
        }
    }

    /**
     * 动态函数
     * @param functionList
     * @param project
     * @param version
     * @param xinhao
     * @return
     */
    public MiniTableRenderData getTcfTable(List<Map<String,String>> functionList ,Integer project, String version, String xinhao, StringBuilder error){
        Style tableHeaderStyle = new Style();
        tableHeaderStyle.setBold(true);
        tableHeaderStyle.setFontSize(10);
        tableHeaderStyle.setColor("000000");

        Style tableRowStyle = new Style();
        tableRowStyle.setFontSize(10);
        tableRowStyle.setColor("000000");

        //tcf_function table
        TableData tcfFunctionData = new TableData();
        tcfFunctionData.setHeader(new RowRenderData(
                Arrays.asList(new TextRenderData("序号", tableHeaderStyle),
                        new TextRenderData("被测模块", tableHeaderStyle),
                        new TextRenderData( "测试用例编号", tableHeaderStyle),
                        new TextRenderData("语句覆盖率", tableHeaderStyle),
                        new TextRenderData( "分支覆盖率", tableHeaderStyle),
                        new TextRenderData( "测试结论", tableHeaderStyle),
                        new TextRenderData("缺陷分析", tableHeaderStyle)),
                "FFFFFF"));
        List<RowRenderData> tcfFunctionTableData = new ArrayList<>();
        String longName = "";
        for(int i=0; i<functionList.size();i++){
            Integer num = i+1;
            String xinhaoRes = xinhao.toUpperCase()+".00_S"+num;
//            String xinhaoRes = "";
//            List<TcfFileEntity> tcfFileEntityList = tcfFileDao.findTcfFileByProcedureFullName(functionList.get(i).toLowerCase(), project, version);
//            if(tcfFileEntityList != null && tcfFileEntityList.size() > 0){
//                xinhaoRes = tcfFileEntityList.get(0).getNum();
//            }else{
//                xinhaoRes = "";
//                // error.append("未找到函数["+functionList.get(i)+"]的测试用例.\r\n");
//                // throw new BusinessException("未找到函数["+functionList.get(i)+"]的测试用例.");
//            }
            // List<CoverageFileEntity> coverageFileList= coverageFileDao.selectByNameAndProjectIdAndVersion(functionList.get(i).toLowerCase(), project, version);
            List<CoverageFileEntity> coverageFileList= coverageFileDao.findCoverageFileByFuncName(functionList.get(i).get("name").toLowerCase(), functionList.get(i).get("hashValue"));
            List<ProblemEntity> problemEntityList = problemDao.selectByNameAndProjectAndVersion(functionList.get(i).get("name").toLowerCase(), project, version);
            String quexian = "无缺陷";
            if(problemEntityList != null && problemEntityList.size() >0){
                quexian = "见问题单";
            }

            longName = lineBreak(functionList.get(i).get("name").toUpperCase(),29);
            if(coverageFileList != null && coverageFileList.size()>0){
                //RowRenderData row = RowRenderData.build(num.toString(), "ASP.READ", "XXXX", "100%", "100%", "正确", "无缺陷");
                RowRenderData row = new RowRenderData(
                        Arrays.asList(new TextRenderData(num.toString(), tableRowStyle),
                                new TextRenderData(longName, tableRowStyle),
                                new TextRenderData( xinhaoRes, tableRowStyle),
                                new TextRenderData(coverageFileList.get(0).getStatementCoverage()+"%", tableRowStyle),
                                new TextRenderData( coverageFileList.get(0).getBranchCoverage()+"%", tableRowStyle),
                                new TextRenderData( "正确", tableRowStyle),
                                new TextRenderData(quexian, tableRowStyle)),
                        "FFFFFF");
                tcfFunctionTableData.add(row);
            }else{
                RowRenderData row = new RowRenderData(
                        Arrays.asList(new TextRenderData(num.toString(), tableRowStyle),
                                new TextRenderData(longName, tableRowStyle),
                                new TextRenderData( xinhaoRes, tableRowStyle),
                                new TextRenderData("", tableRowStyle),
                                new TextRenderData( "", tableRowStyle),
                                new TextRenderData( "正确", tableRowStyle),
                                new TextRenderData(quexian, tableRowStyle)),
                        "FFFFFF");
                tcfFunctionTableData.add(row);
                // error.append("未找到函数["+functionList.get(i)+"]的覆盖率信息.\r\n");
                //throw new BusinessException("未找到函数["+functionList.get(i)+"]的覆盖率信息.");
            }
        }
        tcfFunctionData.setTableDatas(tcfFunctionTableData);
        MiniTableRenderData tcfTable = new MiniTableRenderData(tcfFunctionData.getHeader(), tcfFunctionData.getTableDatas(), 16.49F);
        TableStyle tcfTableStyle = new TableStyle();
        tcfTableStyle.setAlign(STJc.LEFT);
        tcfTable.setStyle(tcfTableStyle);
        return tcfTable;
    }
    public boolean isOverLoad( List<ProcedureDto> list, ProcedureDto dto){
        int cout = 0;
        for(ProcedureDto p: list){
            if(p.getLongName().equals(dto.getLongName())){
                cout++;
            }
        }
        if(cout>1){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 静态函数
     * @param project
     * @param version
     * @return
     */
    public MiniTableRenderData getStaticTable(List<ProcedureDto> staticProcedureList, Integer project, String version){
        Style tableHeaderStyle = new Style();
        tableHeaderStyle.setBold(true);
        tableHeaderStyle.setFontSize(10);
        tableHeaderStyle.setColor("000000");

        Style tableRowStyle = new Style();
        tableRowStyle.setFontSize(10);
        tableRowStyle.setColor("000000");

        //static_function table
        TableData staticFunctionData = new TableData();
        staticFunctionData.setHeader(new RowRenderData(
                Arrays.asList(new TextRenderData("序号", tableHeaderStyle),
                        new TextRenderData("被测模块", tableHeaderStyle),
                        new TextRenderData( "语句覆盖率", tableHeaderStyle),
                        new TextRenderData("分支覆盖率", tableHeaderStyle),
                        new TextRenderData("测试结论", tableHeaderStyle),
                        new TextRenderData("缺陷分析", tableHeaderStyle)),
                "FFFFFF"));
        List<RowRenderData> staticFunctionTableData = new ArrayList<>();

        String longName = "";
        for(int i=0; i<staticProcedureList.size();i++){
            Integer num = i+1;
            longName = lineBreak(staticProcedureList.get(i).getLongName().toUpperCase(), 35);
            List<ProblemEntity> problemEntityList = problemDao.selectByNameAndProjectAndVersion(staticProcedureList.get(i).getLongName().toLowerCase(), project, version);
            String quexian = "无缺陷";
            if(problemEntityList != null && problemEntityList.size() >0){
                quexian = "见问题单";
            }
            //RowRenderData row = RowRenderData.build(num.toString(), "ASP.READ", "XXXX", "100%", "100%", "正确", "无缺陷");
            RowRenderData row = new RowRenderData(
                    Arrays.asList(new TextRenderData(num.toString(), tableRowStyle),
                            new TextRenderData(longName, tableRowStyle),
                            new TextRenderData("100%", tableRowStyle),
                            new TextRenderData( "100%", tableRowStyle),
                            new TextRenderData( "正确", tableRowStyle),
                            new TextRenderData(quexian, tableRowStyle)),
                    "FFFFFF");
            staticFunctionTableData.add(row);
        }
        staticFunctionData.setTableDatas(staticFunctionTableData);

        MiniTableRenderData table = new MiniTableRenderData(staticFunctionData.getHeader(), staticFunctionData.getTableDatas(), 16.49F);
        TableStyle tableStyle = new TableStyle();
        tableStyle.setAlign(STJc.LEFT);
        table.setStyle(tableStyle);

        return table;
    }

    /**
     * 所有函数
     * @param project
     * @param version
     * @return
     */
    public MiniTableRenderData getAllFunctionTable(List<Map<String,String>> functionList ,Integer project, String version, String xinhao){
        Style tableHeaderStyle = new Style();
        tableHeaderStyle.setBold(true);
        tableHeaderStyle.setFontSize(10);
        tableHeaderStyle.setColor("000000");

        Style tableRowStyle = new Style();
        tableRowStyle.setFontSize(10);
        tableRowStyle.setColor("000000");

        TableData allData = new TableData();
        allData.setHeader(new RowRenderData(
                Arrays.asList(new TextRenderData("序号", tableHeaderStyle),
                        new TextRenderData("被测模块", tableHeaderStyle),
                        new TextRenderData( "测试策略", tableHeaderStyle),
                        new TextRenderData("测试用例集", tableHeaderStyle)),
                "FFFFFF"));
        List<RowRenderData> allFunctionTableData = new ArrayList<>();
        Integer num = 0;
        for(int i=0; i<functionList.size();i++){
            num = num+1;
            String xinhaoRes = xinhao.toUpperCase()+".00_S"+num;
//            String xinhaoRes = "";
//            List<TcfFileEntity> tcfFileEntityList = tcfFileDao.findTcfFileByProcedureFullName(functionList.get(i).toLowerCase(), project, version);
//            if(tcfFileEntityList != null && tcfFileEntityList.size() > 0){
//                xinhaoRes = tcfFileEntityList.get(0).getNum();
//            }else{
//                xinhaoRes = "";
//                //throw new BusinessException("未找到函数["+functionList.get(i)+"]的测试用例.");
//            }
            RowRenderData row = new RowRenderData(
                    Arrays.asList(new TextRenderData(num.toString(), tableRowStyle),
                            new TextRenderData(functionList.get(i).get("name").toUpperCase(), tableRowStyle),
                            new TextRenderData( "测试验证", tableRowStyle),
                            new TextRenderData(xinhaoRes, tableRowStyle)),
                    "FFFFFF");
            allFunctionTableData.add(row);
        }

        List<ProcedureDto> procedureEntityList = getStaticFunctionList(project,version);
        String longName = "";
        for(int i=0; i<procedureEntityList.size();i++){
            num = num+1;
            longName = lineBreak(procedureEntityList.get(i).getLongName().toUpperCase(), 35);
            RowRenderData row = new RowRenderData(
                    Arrays.asList(new TextRenderData(num.toString(), tableRowStyle),
                            new TextRenderData(longName, tableRowStyle),
                            new TextRenderData("代码走查", tableRowStyle),
                            new TextRenderData( "", tableRowStyle)),
                    "FFFFFF");
            allFunctionTableData.add(row);
        }
        allData.setTableDatas(allFunctionTableData);

        MiniTableRenderData table = new MiniTableRenderData(allData.getHeader(), allData.getTableDatas(), 14.65F);
        TableStyle tableStyle = new TableStyle();
        tableStyle.setAlign(STJc.LEFT);
        table.setStyle(tableStyle);

        return table;
    }

    public List<SegmentProblemData> getSegmentProblemList(List<Map<String,String>> functionList, List<ProcedureDto> staticProcedureList, Integer project, String version, String xinhao) throws IOException{
        int num = 0;
        List<SegmentProblemData> result = new ArrayList<>();
        List<SegmentProblemData> result1 = new ArrayList<>();
        List<SegmentProblemData> result2 = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for(Map<String,String> fun: functionList){
            list.add(fun.get("name"));
        }
        num = produceSegmentProblemList(list,project,version,xinhao,1, num, result1);
        list.clear();
        for(ProcedureDto fun: staticProcedureList){
            list.add(fun.getLongName());
        }
        produceSegmentProblemList(list,project,version,xinhao,2, num, result2);
        result.addAll(result1);
        result.addAll(result2);
        return result;
    }

    public Integer produceSegmentProblemList(List<String> functionList ,Integer project, String version, String xinhao, Integer type, Integer num, List<SegmentProblemData> segmentProblemDataList) throws IOException{
        //问题单
        for(int i=0; i<functionList.size(); i++){
            List<ProblemEntity> problemEntityList = problemDao.selectByNameAndProjectAndVersion(functionList.get(i).toLowerCase(), project, version);
            for(int j=0; j<problemEntityList.size(); j++){
                ProblemEntity entity = problemEntityList.get(j);
                SegmentProblemData s20 = new SegmentProblemData();
                num = num + 1;
                s20.setProblemNum("" + num);
                Style wugouStyle = new Style();
                wugouStyle.setFontSize(9);
                TextRenderData textRenderDataWuGou = new TextRenderData("□");
                textRenderDataWuGou.setStyle(wugouStyle);
                Style gouStyle = new Style();
                gouStyle.setFontSize(10);
                TextRenderData textRenderDataGou = new TextRenderData("☑");
                textRenderDataGou.setStyle(gouStyle);
                String errorType = entity.getErrorType().toString();
                if(errorType.contains("1")){
                    s20.setError1(textRenderDataGou);
                }else {
                    s20.setError1(textRenderDataWuGou);
                }
                if(errorType.contains("2")){
                    s20.setError2(textRenderDataGou);
                }else {
                    s20.setError2(textRenderDataWuGou);
                }
                if(errorType.contains("3")){
                    s20.setError3(textRenderDataGou);
                }else {
                    s20.setError3(textRenderDataWuGou);
                }
                if(errorType.contains("4")){
                    s20.setError4(textRenderDataGou);
                }else {
                    s20.setError4(textRenderDataWuGou);
                }

                if(entity.getErrorLevel() == 1){
                    s20.setLevel1(textRenderDataGou);
                    s20.setLevel2(textRenderDataWuGou);
                    s20.setLevel3(textRenderDataWuGou);
                }else if(entity.getErrorLevel() == 2){
                    s20.setLevel2(textRenderDataGou);
                    s20.setLevel1(textRenderDataWuGou);
                    s20.setLevel3(textRenderDataWuGou);
                }else if(entity.getErrorLevel() == 3){
                    s20.setLevel3(textRenderDataGou);
                    s20.setLevel2(textRenderDataWuGou);
                    s20.setLevel1(textRenderDataWuGou);
                }

                s20.setFunction(entity.getFuncName().toUpperCase());
                //s20.setTestcaseId(entity.getTestCaseId().toUpperCase());
                if(type == 1){
                    s20.setTestcaseId(xinhao.toUpperCase()+".00_S"+(i+1));
                }else{
                    s20.setTestcaseId("");
                }
                s20.setTestEnvironment(entity.getTestEnvironment());
                s20.setTestCaseName(entity.getTestCaseName());
                s20.setTestInput(entity.getTestInput());
                s20.setProblemDescription(entity.getProblemDescription());
                s20.setSuggest(entity.getSuggest());
                s20.setSolution(entity.getSolution());
                s20.setClosedCycleResult(entity.getClosedCycleResult());
                s20.setTestPerson(entity.getTestPerson());
                s20.setTestTime(entity.getTestTime());
                s20.setRemark(entity.getRemark());

                JSONArray propertyArray = JSONArray.parseArray(entity.getOther());
                List<RowRenderData> properties = new ArrayList<>();
                ProblemPropertyData pp = new ProblemPropertyData();
                //RowRenderData good = RowRenderData.build("新的属性1", "墙纸");
                Style propertyStyle = new Style();
                propertyStyle.setFontSize(9);
                propertyStyle.setFontFamily("宋体 (中文正文)");
                for(int v=0; v<propertyArray.size(); v++){
                    RowRenderData good = new RowRenderData(Arrays.asList(new TextRenderData(propertyArray.getJSONObject(v).getString("label"),propertyStyle), new TextRenderData(propertyArray.getJSONObject(v).getString("value"),propertyStyle)),"FFFFFF");
                    properties.add(good);
                }
                pp.setProperties(properties);
                s20.setProblemProperty(pp);

                segmentProblemDataList.add(s20);
            }
        }
        return num;
    }

    public String getPicPath(String fun, String hashValue, CoverageType type, Integer project, String version, StringBuilder error) {
        // List<PictureFileEntity> res = pictureFileDao.selectByNameAndProjectIdAndVersion(fun, type.getType(), project, version);
        List<PictureFileEntity> res = pictureFileDao.findPictureFileByFuncNameAndType(fun.toLowerCase(), type.getType(), hashValue);
        if(res!=null && res.size()>0){
            return uploadPath+File.separator+res.get(0).getFileUrl();
        }else{
            error.append("未找到函数["+fun+"]的"+type.getName()+"图片路径.\r\n");
            return "";
            //throw new BusinessException("未找到函数["+fun+"]的"+type.getName()+"图片路径.");
        }
    }

    public List<SegmentPictureData> getSegmentPictureDataList(List<Map<String,String>> mubanFunctionList, Integer project, String version, StringBuilder error) throws IOException{
        //截图文件
        List<SegmentPictureData> segmentPictureDataList = new ArrayList<>();
        CoverageType type = CoverageType.TESTCASE;
        //double standWidth = 600.0 / 994;
        int standWidth = 600;
        double standHeight = 80.0 / 89;
        for(int i=0; i<mubanFunctionList.size(); i++) {
            SegmentPictureData s10 = new SegmentPictureData();
            Map<String,String> fun = mubanFunctionList.get(i);
            s10.setFunction(fun.get("name"));

            try{

                type = CoverageType.TESTCASE;
                String testcasePath = getPicPath(fun.get("name"), fun.get("hashValue"), type, project, version, error);
                if(!"".equals(testcasePath)){
                    String testcaseType = testcasePath.substring(testcasePath.lastIndexOf("."));
                    BufferedImage bufferedImageTestcase = ImageIO.read(new File(testcasePath));
                    //int testcaseWidth = bufferedImageTestcase.getWidth();
                    int testcaseHeight = bufferedImageTestcase.getHeight();
                    s10.setTestcasePic(new PictureRenderData(standWidth, (int) (standHeight * testcaseHeight), testcaseType, bufferedImageTestcase));
                }

                type = CoverageType.STATEMENT;
                String statementPath = getPicPath(fun.get("name"), fun.get("hashValue"), type, project, version, error);
                if(!"".equals(statementPath)){
                    String statementType = testcasePath.substring(testcasePath.lastIndexOf("."));
                    BufferedImage bufferedImageStatement = ImageIO.read(new File(statementPath));
                    //int statementWidth = bufferedImageStatement.getWidth();
                    int statementHeight = bufferedImageStatement.getHeight();
                    s10.setStatementPic(new PictureRenderData(standWidth, (int) (standHeight * statementHeight), statementType,bufferedImageStatement));
                }

                type = CoverageType.BRANCH;
                String branchPath = getPicPath(fun.get("name"), fun.get("hashValue"), type, project, version, error);
                if(!"".equals(branchPath)){
                    String branchType = testcasePath.substring(testcasePath.lastIndexOf("."));
                    BufferedImage bufferedImageBranch = ImageIO.read(new File(branchPath));
                    //int branchWidth = bufferedImageBranch.getWidth();
                    int branchHeight = bufferedImageBranch.getHeight();
                    s10.setBranchPic(new PictureRenderData(standWidth, (int) (standHeight * branchHeight), branchType, bufferedImageBranch));
                }
                segmentPictureDataList.add(s10);
            }catch(Exception e) {
                throw new BusinessException("未找到函数["+fun.get("name")+"]的"+type.getName()+"图片文件,请检查文件是否已被移动.");
            }
        }
        return segmentPictureDataList;
    }


    public void renderPic(XWPFDocument doc, List<Map<String,String>> mubanFunctionList, int project, String version, StringBuilder error){
        for(int i=0; i<mubanFunctionList.size(); i++){
            XWPFParagraph p1 = doc.createParagraph();
            p1.setStyle("2");
            XWPFRun run1 = p1.createRun();
            run1.setText(mubanFunctionList.get(i).get("name")+"测试覆盖率结果");

            XWPFParagraph p2 = doc.createParagraph();
            p2.setStyle("3");
            XWPFRun run2 = p2.createRun();
            run2.setText("测试路径概要");

            XWPFParagraph p3 = doc.createParagraph();
            XWPFRun run3 = p3.createRun();
            CoverageType type = CoverageType.TESTCASE;
            String path = getPicPath(mubanFunctionList.get(i).get("name"), mubanFunctionList.get(i).get("hashValue"), type, project, version, error);
            addPicItem(run3,path,mubanFunctionList.get(i).get("name"),type);

            XWPFParagraph p4 = doc.createParagraph();
            p4.setStyle("3");
            XWPFRun run4 = p4.createRun();
            run4.setText("语句覆盖率");

            XWPFParagraph p5 = doc.createParagraph();
            XWPFRun run5 = p5.createRun();
            type = CoverageType.STATEMENT;
            path = getPicPath(mubanFunctionList.get(i).get("name"), mubanFunctionList.get(i).get("hashValue"), type, project, version, error);
            addPicItem(run5,path,mubanFunctionList.get(i).get("name"),type);

            XWPFParagraph p6 = doc.createParagraph();
            p6.setStyle("3");
            XWPFRun run6 = p6.createRun();
            run6.setText("分支覆盖率");

            XWPFParagraph p7 = doc.createParagraph();
            XWPFRun run7 = p7.createRun();
            type = CoverageType.BRANCH;
            path = getPicPath(mubanFunctionList.get(i).get("name"), mubanFunctionList.get(i).get("hashValue"), type, project, version, error);
            addPicItem(run7,path,mubanFunctionList.get(i).get("name"),type);
        }
    }

    public void addPicItem(XWPFRun run, String path, String fun, CoverageType type){
        int standWidth = 500;
        double standHeight = 80.0 / 89;
        try{
            if(!"".equals(path)){
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                int height = bufferedImage.getHeight();
                String picType = path.substring(path.lastIndexOf("."));
                picType = picType.toLowerCase();
                String name = path.substring(path.lastIndexOf(File.separator)+1);
                ByteArrayInputStream ins = new ByteArrayInputStream(BytePictureUtils.getBufferByteArray(bufferedImage));
                run.addPicture((InputStream) ins,ListPicturePolicy.suggestFileType(picType),name,Units.toEMU(standWidth), Units.toEMU(standHeight * height));
            }else{
                run.setText("");
            }
        }catch(IOException e){
            throw new BusinessException("未找到函数["+fun+"]的"+type.getName()+"图片文件,请检查文件是否已被移动.");
        }catch (InvalidFormatException e){
            throw new BusinessException(e.getMessage());
        }

    }

    public List<ProcedureDto> getStaticFunctionList(int project, String version){
        List<ProcedureDto> staticProcedureList = procedureDao.findProcedureListByIdAndVersionFull(project,version,"true", null, null, null,null);
        for(ProcedureDto dto: staticProcedureList){
            if(procedureService.isOverloadedFun(project,version,dto.getLongName())){
                dto.setLongName(dto.getFullName().replace("(","_").replace(")","").replace(",","_").toLowerCase());
            }
        }
        for(ProcedureDto dto: staticProcedureList){
            System.out.println(dto.getLongName());
        }
        return staticProcedureList;
    }

    public List<Map<String, String>> getMubanFunctionList(XWPFDocument doc, Integer fileType, List<Map<String, String>> list, List<ProcedureDto> procedureDtoList){
        XWPFWordExtractor s = new XWPFWordExtractor(doc);
//        List<String> list = new ArrayList<>();
        List<XWPFParagraph> paras = doc.getParagraphs();
        for (int i=0; i<paras.size(); i++) {
            XWPFParagraph graph = paras.get(i);
            String text = graph.getParagraphText();
            String style = graph.getStyle();
            if ("1".equals(style)) {
                System.out.println(text + "--[" + style + "]");
//                if(text.contains("模块测试") && fileType == 2){
//                    list.add(extractEn(text));
//                }
            } else if ("2".equals(style)) {
                System.out.println(text + "--[" + style + "]");
                if(text.contains("模块测试")){
                    String name = extractEn(text);
                    int k=0;
                    for(; k<procedureDtoList.size(); k++){
                        if(name.toLowerCase().equals(procedureDtoList.get(k).getLongName()) || name.toLowerCase().equals(procedureDtoList.get(k).getFullName().replace("(","_").replace(")","").replace(",","_"))){
                            Map<String,String> item = new HashMap<>();
                            item.put("name",name);
                            item.put("hashValue", procedureDtoList.get(k).getHashValue());
                            list.add(item);
                            break;
                        }
                    }
                    if(k==procedureDtoList.size()){
                        Map<String,String> item = new HashMap<>();
                        item.put("name",name);
                        item.put("hashValue", "");
                        list.add(item);
                    }
                }
            } else if ("3".equals(style)) {
                System.out.println(text + "--[" + style + "]");
            } else {
                continue;
            }
        }
//        for(int i=0; i<list.size(); i++){
//            for(int j=0; j<list.size(); j++){
//                if(i!=j && list.get(i) != null && list.get(i).equals(list.get(j))){
//                    if(fileType == 1){
//                        throw new BusinessException("测试报告,word模板文件中存在相同的模块名[" + list.get(i) + "].");
//                    }else{
//                        throw new BusinessException("说明报告,word模板文件中存在相同的模块名[" + list.get(i) + "].");
//                    }
//                }
//            }
//        }
        return list;
    }

    public boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    public String extractEn(String s1) {
        StringBuilder sb = new StringBuilder(s1);
        IntStream.range(0, s1.length()).forEach(k -> {
            char c = sb.charAt(k);
            if (!isChineseChar(c))
                sb.append(c);
        });
        return sb.toString().substring(s1.length(), sb.length());
    }

    public ListTextData getTestCase(List<Map<String,String>> functionList, Integer project, String version, String xinhao, StringBuilder error){
        ListTextData data = new ListTextData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
        for(int i=0; i<functionList.size(); i++){
            //list.add(xinhao.toUpperCase()+".00_S"+(i+1));
            //String xinhaoRes = "";
            List<TcfFileEntity> tcfFileEntityList = tcfFileDao.findTcfFileByFuncName(functionList.get(i).get("name").toLowerCase(), functionList.get(i).get("hashValue"));
            if(tcfFileEntityList != null && tcfFileEntityList.size() > 0){
                //xinhaoRes = tcfFileEntityList.get(0).getNum();
            }else{
                //xinhaoRes = "";
                error.append("未找到函数["+functionList.get(i).get("name")+"]的测试用例.\r\n");
                //throw new BusinessException("未找到函数["+functionList.get(i)+"]的测试用例.");
            }
            list.add(xinhao.toUpperCase()+".00_S"+(i+1));
        }
        data.setTextList(list);
        return data;
    }
    public ListTextData getTestCase2(List<Map<String,String>> functionList, Integer project, String version, String xinhao, StringBuilder error){
        ListTextData data = new ListTextData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
        for(int i=0; i<functionList.size(); i++){
            //list.add(xinhao.toUpperCase()+".00_S"+(i+1));
            String xinhaoRes = xinhao.toUpperCase()+".00_S"+(i+1);
            // List<TcfFileEntity> tcfFileEntityList = tcfFileDao.findTcfFileByProcedureFullName(functionList.get(i).toLowerCase(), project, version);
            List<TcfFileEntity> tcfFileEntityList = tcfFileDao.findTcfFileByFuncName(functionList.get(i).get("name").toLowerCase(), functionList.get(i).get("hashValue"));
            if(tcfFileEntityList != null && tcfFileEntityList.size() > 0){
                //xinhaoRes = tcfFileEntityList.get(0).getNum();
            }else{
                //xinhaoRes = "";
                error.append("未找到函数["+functionList.get(i).get("name")+"]的测试用例.\r\n");
                //throw new BusinessException("未找到函数["+functionList.get(i)+"]的测试用例.");
            }
            list.add(xinhaoRes);
        }
        List<String> listResult = new ArrayList<>();
        listResult.addAll(list);
        listResult.addAll(list);
        data.setTextList(listResult);
        return data;
    }

    public void getInputOutPut(List<Map<String, String>> functionList ,ListTextData inputData,ListTextData outputData,ListTextData descData,Integer project, String version, StringBuilder error, Integer fileType){
        inputData.setCurrent(0);
        outputData.setCurrent(0);
        descData.setCurrent(0);
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();

        for(Map<String, String> fun: functionList){
            List<FunDescEntity> funDescEntityList = funDescDao.selecByName(fun.get("name").toLowerCase(), project, version);

            if(funDescEntityList != null && funDescEntityList.size()>0){
                String input = funDescEntityList.get(0).getInputDescription();
                String output = funDescEntityList.get(0).getOutputDescription();
                String desc = funDescEntityList.get(0).getComment();
                list1.add(input);
                list2.add(output);
                list3.add(desc);
                if(input == null || input.isEmpty()){
                    error.append("函数["+fun.get("name")+"]的输入说明为空.\r\n");
                }
                if(output == null || output.isEmpty()){
                    error.append("函数["+fun.get("name")+"]的输出说明为空.\r\n");
                }
                if(desc == null || desc.isEmpty()){
                    if(fileType == 2){
                        error.append("函数["+fun.get("name")+"]的描述信息为空.\r\n");
                    }
                }
            }else{
                list1.add("");
                list2.add("");
                list3.add("");
                error.append("未找到函数["+fun.get("name")+"]的输入说明.\r\n");
                //throw new BusinessException("未找到函数["+fun+"]的输入说明");
                error.append("未找到函数["+fun.get("name")+"]的输出说明.\r\n");
                //throw new BusinessException("未找到函数["+fun+"]的输出说明");
                if(fileType == 2){
                    error.append("未找到函数["+fun.get("name")+"]的描述信息.\r\n");
                    //throw new BusinessException("未找到函数["+fun+"]的描述信息.");
                }
            }
        }

        inputData.setTextList(list1);
        outputData.setTextList(list2);
        descData.setTextList(list3);
    }

    /**
     * 根据函数名获取图片列表
     * @param functionList
     * @param type 1：测试用例，2：语句覆盖率，3：分支覆盖率
     * @param project
     * @param version
     * @return
     */
    public ListPictureData getPic(List<Map<String, String>> functionList,CoverageType type ,Integer project, String version){
        ListPictureData data = new ListPictureData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
//        list.add(mubanPath + File.separator+"Case_ReadCom.png");
//        list.add(mubanPath + File.separator+"Case_ReadCom.png");
        for(Map<String, String> fun: functionList){
            // List<PictureFileEntity> res = pictureFileDao.selectByNameAndProjectIdAndVersion(fun, type.getType(), project, version);
            List<PictureFileEntity> res = pictureFileDao.findPictureFileByFuncNameAndType(fun.get("name").toLowerCase(), type.getType(), fun.get("hashValue"));
            if(res!=null && res.size()>0){
                list.add(uploadPath+File.separator+res.get(0).getFileUrl());
            }else{
                list.add("");
                throw new BusinessException("未找到函数["+fun.get("name")+"]的"+type.getName()+"图片");
            }
        }
        data.setPictureList(list);
        return data;
    }

    public ListTextData getStatementCoverage(List<Map<String,String>> functionList ,Integer project, String version, StringBuilder error){
        ListTextData data = new ListTextData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
        for(Map<String,String> fun: functionList){
            // List<CoverageFileEntity> res = coverageFileDao.selectByNameAndProjectIdAndVersion(fun, project, version);
            List<CoverageFileEntity> res = coverageFileDao.findCoverageFileByFuncName(fun.get("name").toLowerCase(), fun.get("hashValue"));
            if(res != null && res.size()>0){
                list.add(res.get(0).getStatementCoverage()+"%");
            }else{
                list.add("");
                error.append("未找到函数["+fun.get("name")+"]的语句覆盖率.\r\n");
                //throw new BusinessException("未找到函数["+fun+"]的语句覆盖率");
            }
        }
        data.setTextList(list);
        return data;
    }

    public ListTextData getBranchCoverage(List<Map<String,String>> functionList ,Integer project, String version, StringBuilder error){
        ListTextData data = new ListTextData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
        for(Map<String,String> fun: functionList){
            // List<CoverageFileEntity> res = coverageFileDao.selectByNameAndProjectIdAndVersion(fun, project, version);
            List<CoverageFileEntity> res = coverageFileDao.findCoverageFileByFuncName(fun.get("name").toLowerCase(), fun.get("hashValue"));
            if(res != null && res.size()>0){
                list.add(res.get(0).getBranchCoverage()+"%");
            }else{
                list.add("");
                error.append("未找到函数["+fun.get("name")+"]的分支覆盖率.\r\n");
                //throw new BusinessException("未找到函数["+fun+"]的分支覆盖率");
            }
        }
        data.setTextList(list);
        return data;
    }

    public ListTextData getFunctionName(List<String> functionList){
        ListTextData data = new ListTextData();
        data.setCurrent(0);
        List<String> list = new ArrayList<>();
        for(String fun: functionList){
            list.add(fun.toUpperCase());
        }
        data.setTextList(list);
        return data;
    }

    public String getWordMubanUrl(Integer project, String version, Integer type){
        String url="";
        List<WordMubanEntity> res = wordMubanDao.selectByProjectIdAndVersion(project, version, type);
        if(res!=null && res.size()>0){
            url = res.get(0).getFileUrl();
        }else{
            throw new BusinessException("未找到生成报告的模板文件");
        }
        return uploadPath+File.separator+url;
    }

    public void insert(ReportEntity report){
        reportDao.insert(report);
    }

    public List<ReportEntity> selectAllReport(Integer project, String version, int pageIndex, int pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        List<ReportEntity> list = reportDao.findProcedureListByIdAndVersion(project, version);
        return list;
    }
}
