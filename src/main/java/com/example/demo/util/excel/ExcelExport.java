package com.example.demo.util.excel;
/*
 * <p>项目名称: SpringBoot-Shiro-Vue </p>
 * <p>包名称: com.heeexy.example.util.excel </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/5/7 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Map;

public class ExcelExport {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private Row currentRow;
    private int rowNum;
    private int columnNum;
    private int rowSize;
    private int columnSize;
    private Map<Integer, Map<Integer,Integer>> mergedCellMap;

    public HSSFWorkbook getWorkbook(){
        return this.workbook;
    }
    public HSSFSheet getSheet(){
        return this.sheet;
    }

    public ExcelExport(int columnSize){
        this.columnSize = columnSize;
        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.mergedCellMap = new HashMap<>();
    }

    private Cell createCellAt(int position){
        return currentRow.createCell(position);
    }

    private void moveToNextColumn(){
        columnNum++;
        Map<Integer, Integer> map = mergedCellMap.get(rowNum);
        if(map != null){
            while (map.containsKey(columnNum)){
                columnNum++;
            }
        }
    }

    public Cell createCell(){
        if(currentRow == null){
            throw new RuntimeException("Please create a row first!");
        }
        Cell cell = createCellAt(this.columnNum);
        moveToNextColumn();
        return cell;
    }

    public Cell createCell(int columnCount, int rowCount){
        int finalRow = rowNum + rowCount - 1;
        int finalColumn = columnNum + columnCount - 1;
        sheet.addMergedRegion(new CellRangeAddress(rowNum, finalRow,columnNum,finalColumn));
        int currRow = rowNum;
        int currCol = columnNum;
        for(int i=0; i<rowCount-1; i++){
            currRow++;
            if(!mergedCellMap.containsKey(currRow)){
                mergedCellMap.put(currRow,new HashMap<>());
            }
            Map<Integer, Integer> map = mergedCellMap.get(currRow);
            for(int j=0; j<columnCount; j++){
                int col = currCol+j;
                if(!map.containsKey(col)){
                    map.put(col,col);
                }
            }
        }
        Cell cell = createCellAt(columnNum);
        columnNum = finalColumn;
        moveToNextColumn();
        return cell;
    }

    public Row createNextRow(){
        HSSFRow row = sheet.createRow(rowSize);
        currentRow = row;
        rowNum = rowSize;
        columnNum = 0;
        Map<Integer, Integer> map=mergedCellMap.get(rowNum);
        if(map != null){
            for(int i=0; i<columnSize; i++){
                if(!map.containsKey(i)){
                    columnNum = i;
                    break;
                }
            }
        }
        rowSize++;
        return row;
    }
}
