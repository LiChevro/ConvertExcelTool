package com.hisuntech.utils;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Description  通过POI读取Excel文件
 * @author hx
 * @create 2018.8.6
 */
public class TransferExcelUtil {

    private final static String TABLE_ENNAME = "英文表名";              //英文表名
    private final static String TABLE_CHNAME = "中文表名";              //中文表名
    //private final static String DARABASE_NAME = "数据库名称";              //数据库名
    private final static String DARABASE_BRAND = "数据库";                  //数据库商（oracle,mysql,db2,sql server）
    private final static String TABLE_SPACE = "表空间";                      //表空间

    private final static String SEQUENCE_NUMBER = "序号";                 //序号
    private final static String FIELD_ENNAME = "字段（英文）";            //字段英文名
    private final static String FIELD_CHNAME = "字段中文名";            //字段中文名
    private final static String FIELD_TYPE = "类型";                      //字段类型
    private final static String IS_NULLABLE = "是否可空";               //字段是否为空（是，否）
    private final static String IS_PRIMARYKEVALUE = "是否主键";          //字段是否是主键（是，否）
    private final static String DEFAULT_VALUE = "默认值";              //字段的默认值
    private final static String INDEX_NUM = "索引编号（数字）";         //索引编号（数字）
    private final static String INDEX_TYPE = "索引类型";                //索引类型（D，U）
    private final static String FIELD_DESCRIPTION = "字段说明";         //字段说明

    private final static String TABLE_INFOMATION= "数据库信息";         //数据库信息
    private final static String TABLE_NAME = "表名";         //数据库表名

    /*public static void main(String[] agrs){
        List<Table> ls = readExcel("D:/测试文件夹/aaa.xlsx");
        for (int i = 0; i < ls.size(); i++) {
            System.out.println(ls.get(i).getTableEnName()+"\t"+"0.0！");
            System.out.println(ls.get(i).getTableChName()+"\t"+"0.0！");
            System.out.println(ls.get(i).getDatabaseBrand()+"\t"+"0.0！");
            System.out.println(ls.get(i).getTableSpace()+"\t"+"0.0！");
            List<Field> flist = ls.get(i).getFields();
            for (int j = 0; j < flist.size(); j++) {
                System.out.println(flist.get(j).toString());
            }
        }
    }*/

    public static List<Table> readExcel(String path) {
        InputStream input = null;   //输入流
        Workbook wb = null;         //表对象
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        Table table = null;         //每个sheet对应一个table对象
        Field field = null;         //字段对象
        List<Table> tableList = new ArrayList<Table>();
        List<Field> fieldList = null;
        Map<String, List<String>> map = null;
        List<String> list = null;

        try {
            input = new FileInputStream(path);
            wb = new XSSFWorkbook(input);
            List<String> tNameList = findTableName(wb); //找到表名目录
            int sheetSize = wb.getNumberOfSheets();//总sheet数

            for (int sheetNum = 0; sheetNum < sheetSize; sheetNum++) { //遍历每个sheet
                sheet = wb.getSheetAt(sheetNum);
                for (int i = 0; i < tNameList.size(); i++) {
                    if (tNameList.get(i).equals(sheet.getSheetName())) {    //匹配对应的表名
                        //System.out.println(tNameList.get(i)+"\t"+wb.getSheetAt(sheetNum).getSheetName());
                        table = new Table();
                        int rowNums = sheet.getLastRowNum();//总行数
                        int coloumNums = sheet.getRow(0).getPhysicalNumberOfCells();//总列数
                        fieldList = new ArrayList<Field>();
                        map = new HashMap<String, List<String>>();
                        boolean flag = false;//找到各字段所在行的标志
                        int rowflag = 0;

                        for (int rowNum = 0; rowNum < rowNums+1 ; rowNum++) {   //遍历行，实际要多一行
                            row = sheet.getRow(rowNum);
                            if (row == null){
                                continue;
                            }else if ((row.getCell(0) == null || "".equals(cellType(row.getCell(0)).getStringCellValue()))
                                    &&(row.getCell(1) == null || "".equals(cellType(row.getCell(1)).getStringCellValue()))
                                    &&(row.getCell(2) == null || "".equals(cellType(row.getCell(2)).getStringCellValue()))){
                                continue;       //若前三个单元格为空，则跳过当前行
                            }else if (flag) {
                                field = new Field();
                                int count = rowNum-rowflag;//从标志行开始往下读取
                                //System.out.println(flag+"\t"+rowflag);
                                fieldList.add(addField(map, field, count));//每一行作为对象存入集合中
                                continue;
                            }

                            for (int coloumNum = 0; coloumNum < coloumNums ; coloumNum++) {//遍历每一行的单元格
                                list = new ArrayList<String>();
                                cell = row.getCell(coloumNum);
                                String cellvalue = cellType(cell).getStringCellValue();//Cell转为String类型
                                if(cellvalue.contains(SEQUENCE_NUMBER)) {//根据序号判断标志行
                                    flag = true;
                                    rowflag = rowNum+1;
                                }
                                //把与数据库相关的信息存入table中
                                addSheetHead(cellvalue, table, row, coloumNum);
                                //把字段集合存到map中
                                putField(map, list, cellvalue, sheet, row, cell,rowNum, rowNums, coloumNum);
                            }
                        }
                        table.setFields(fieldList);
                        //System.out.println(table.getFields().toString());
                        tableList.add(table);
                    }
                }
                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableList;
    }

    private static List<String> findTableName(Workbook wb){
        List<String> tNameList = new ArrayList<String>();  //表名集合
        Sheet dbsheet = wb.getSheet(TABLE_INFOMATION);
        int rowNums = dbsheet.getLastRowNum();
        int coloumNums = dbsheet.getRow(0).getPhysicalNumberOfCells();
        for (int rowNum = 0; rowNum < rowNums+1; rowNum++) {
            Row row = dbsheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            for (int coloumNum = 0; coloumNum < coloumNums; coloumNum++) {
                Cell cell = row.getCell(coloumNum);
                String cellvalue = cellType(cell).getStringCellValue();
                //int count = 0;
                if (cellvalue.contains(TABLE_NAME)){
                    for (int i = rowNum + 1; i < rowNums + 1; i++) {
                        if (dbsheet.getRow(i).getCell(coloumNum).getStringCellValue() == null ||
                                "".equals(dbsheet.getRow(i).getCell(coloumNum).getStringCellValue())){
                            continue;
                        }
                        tNameList.add(dbsheet.getRow(i).getCell(coloumNum).getStringCellValue());
                        //System.out.println(tNameList.get(count++));
                    }
                }
            }
        }
        return tNameList;
    }

    private static Field addField(Map<String, List<String>> map, Field field, int count) {
        field.setFieldEnName(map.get(FIELD_ENNAME).get(count));
        field.setFieldChName(map.get(FIELD_CHNAME).get(count));
        field.setFieldType(map.get(FIELD_TYPE).get(count));
        field.setIsNullAble(map.get(IS_NULLABLE).get(count));
        field.setIsPrimaryKey(map.get(IS_PRIMARYKEVALUE).get(count));
        field.setDefaultValue(map.get(DEFAULT_VALUE).get(count));
        field.setIndexNum(map.get(INDEX_NUM).get(count));
        field.setIndexType(map.get(INDEX_TYPE).get(count));
        field.setFieldDescription(map.get(FIELD_DESCRIPTION).get(count));
        return field;
    }

    private static void addSheetHead(String cellvalue, Table table, Row row, int coloumNum){
        if(cellvalue.contains(TABLE_ENNAME)) {
            table.setTableEnName(row.getCell(coloumNum + 1).getStringCellValue());
            //System.out.println("这里取出英文表名的值："+table.getTableEnName());
        } else if(cellvalue.contains(TABLE_CHNAME)){
            table.setTableChName(row.getCell(coloumNum+1).getStringCellValue());
        } else if(cellvalue.contains(DARABASE_BRAND)){
            table.setDatabaseBrand(row.getCell(coloumNum+2).getStringCellValue());//数据库占了两个单元格，值存在前一个单元格中
        } else if(cellvalue.contains(TABLE_SPACE)){
            table.setTableSpace(row.getCell(coloumNum+1).getStringCellValue());
            //System.out.println("这里取出表空间的值："+table.getTableSpace());
        }
    }

    private static void putField(Map<String, List<String>> map, List<String> list, String cellvalue,
                                 Sheet sheet, Row row, Cell cell,
                                 int rowNum, int rowNums, int coloumNum){

        if(cellvalue.contains(FIELD_ENNAME)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(FIELD_ENNAME, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(FIELD_CHNAME)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(FIELD_CHNAME, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(FIELD_TYPE.equals(cellvalue)){          //数字类型
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(cellType(sheet.getRow(i).getCell(coloumNum)).getStringCellValue());
            }
            map.put(FIELD_TYPE, list);
            //System.out.println(map.get(FIELD_TYPE)+"进来了没有？？");
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(IS_NULLABLE)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(IS_NULLABLE, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(IS_PRIMARYKEVALUE)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(IS_PRIMARYKEVALUE, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(DEFAULT_VALUE)){   //数字类型
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(cellType(sheet.getRow(i).getCell(coloumNum)).getStringCellValue());
            }
            map.put(DEFAULT_VALUE, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(INDEX_NUM)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {    //数字类型
                list.add(cellType(sheet.getRow(i).getCell(coloumNum)).getStringCellValue());
            }
            map.put(INDEX_NUM, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(INDEX_TYPE)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(INDEX_TYPE, list);
            //System.out.println(map.get(INDEX_TYPE)+"这里进来了没有？？");
            //System.out.println(cellvalue+"\t"+coloumNum);
        } else if(cellvalue.contains(FIELD_DESCRIPTION)){
            for (int i = rowNum + 1; i < rowNums + 1; i++) {
                list.add(sheet.getRow(i).getCell(coloumNum).getStringCellValue());
            }
            map.put(FIELD_DESCRIPTION, list);
            //System.out.println(cellvalue+"\t"+coloumNum);
        }
    }

    private static Cell cellType(Cell cell){
        switch (cell.getCellTypeEnum()){
            case NUMERIC:
                cell.setCellType(CellType.STRING);
                break;
            case BOOLEAN:
                cell.setCellType(CellType.STRING);
                break;
            default:
                break;
        }
        return cell;
    }

    //测试
    public static void main(String[] args) {
        String path = "F:\\HiSunTech\\template.xlsx";
        List<Table> tableList = new ArrayList<>();
        tableList = readExcel(path);
        Map<String,List> map = GenerateSQLVersion1.outSql(tableList);
        //1.生成创建表的SQL
        List<StringBuffer> sqlList = map.get("createSqlList");
        //2.设置主键的SQL
        List<StringBuffer> sqlList2 = map.get("primarySqlList");
        //3.注释的SQL
        List<StringBuffer> sqlList3 = map.get("commentSQL");
        //4.生成索引
        List<StringBuffer> sqlList4 = null;
        //5.输出到文件
        try {
            OutToFileUtil.outToFile(sqlList,sqlList2,sqlList3,sqlList4,tableList,"E://");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
