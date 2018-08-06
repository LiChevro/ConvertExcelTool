package com.hisuntech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import com.hisuntech.entity.Version;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Description 读取数据库的版本信息
 */
public class ReadExcelVersion {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     * @param in
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(InputStream in,File file) throws IOException{
        Workbook wb = null;
        if(file.getName().endsWith(EXCEL_XLS)){  //Excel 2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断文件是否是excel
     * @throws Exception
     */
    public static void checkExcelVaild(File file) throws Exception{
        if(!file.exists()){
            throw new Exception("文件不存在");
        }
        if(!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))){
            throw new Exception("文件不是Excel");
        }
    }
    /**
     * 根据输入的路径文件返回Version类型列表
     * @param file
     * @return
     */
    public static List<Version> ReadExcelSheel(File file){


        List<Version> list=new ArrayList<Version>();
        try {
            // 同时支持Excel 2003、2007
            File excelFile =file;// 创建文件对象
            FileInputStream in = new FileInputStream(excelFile); // 文件流
            checkExcelVaild(excelFile);
            Workbook workbook = getWorkbok(in,excelFile);
            //Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel2003/2007/2010都是可以处理的
            int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
            Sheet sheet = workbook.getSheetAt(0);   // 遍历第1个Sheet
            int rowsOfSheet = sheet.getPhysicalNumberOfRows();  //sheel的行数
            int columnNum=sheet.getRow(0).getPhysicalNumberOfCells();  //sheel的列数
            String[][] strarr=new String[rowsOfSheet][columnNum];    //字符串数组

            /**
             * 将sheel内容提取到字符串数组
             */
            for (int i = 0; i < rowsOfSheet; i++) {
                Row row=sheet.getRow(i);
                for (int j = 0; j < columnNum; j++) {

                    Cell cell = row.getCell(j);
                    if (cell!=null) {
                        strarr[i][j]=cell.toString();
                    }else {
                        strarr[i][j]="null";
                    }

                }
            }

            int beginW=0;  //更新时间开始的列
            boolean isbegin=false;   //是否开始读内容
            boolean isend=false;		//是否结束读内容
            in:
            for (int i = 0; i < rowsOfSheet; i++) {
                for (int j = 0; j < columnNum; j++) {
                    if (isbegin&&!false) {
                        if (strarr[i][beginW].equals("")&&strarr[i][beginW+1].equals("")&&strarr[i][beginW+2].equals("")&&strarr[i][beginW+3].equals("")) {
                            isend=true;
                            break in;
                        }
                        Version version=new Version(strarr[i][beginW],strarr[i][beginW+1],strarr[i][beginW+2],strarr[i][beginW+3]);
                        list.add(version);
                        break;
                    }
                    if (strarr[i][j].equals("更新时间")) {
                        beginW=j;
                        isbegin=true;
                        break;
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

}
