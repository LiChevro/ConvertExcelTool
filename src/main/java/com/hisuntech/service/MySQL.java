package com.hisuntech.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ll
 * @since 2018.8.20
 */
public class MySQL {

    //数值类型
    private final String INT = "INT";
    private final String TINYINT = "TINYINT";
    private final String SMALLINT = "SMALLINT";
    private final String MEDIUMINT = "MEDIUMINT";
    private final String INTEGER = "INTEGER";
    private final String BIGINT = "BIGINT";
    private final String FLOAT = "FLOAT";
    private final String DECIMAL = "DECIMAL";

    //字符串类型
    private final String CHAR = "CHAR";
    private final String VARCHAR = "VARCHAR";
    private final String TINYBLOB = "TINYBLOB";
    private final String TINYTEXT = "TINYTEXT";
    private final String BLOB = "BLOB";
    private final String TEXT = "TEXT";
    private final String MEDIUMBLOB = "MEDIUMBLOB";
    private final String MEDIUMTEXT = "MEDIUMTEXT";
    private final String LOGNGBLOB = "LOGNGBLOB";
    private final String LONGTEXT = "LONGTEXT";
    private final String VARBINARY = "VARBINARY";
    private final String BINARY = "BINARY";

    //日期类型
    private final String DATE = "DATE";
    private final String TIME  = "BINARY";
    private final String YEAR = "YEAR";
    private final String DATETIME = "DATETIME";
    private final String TIMESTAMP = "TIMESTAMP";

    /**
     * @return 字符串类型的list
     */
    public List<String> getStringTypeList(){
        List<String> list = new ArrayList<>();
        list.add(CHAR);
        list.add(VARCHAR);
        list.add(TINYBLOB);
        list.add(TINYTEXT);
        list.add(BLOB);
        list.add(TEXT);
        list.add(MEDIUMBLOB);
        list.add(MEDIUMTEXT);
        list.add(LOGNGBLOB);
        list.add(LONGTEXT);
        list.add(VARBINARY);
        list.add(BINARY);
        return list;
    }

    /**
     * @return 日期类型的list
     */
    public List<String> getDateTypeList(){
        List<String> list = new ArrayList<>();
        list.add(DATE);
        list.add(TIME);
        list.add(YEAR);
        list.add(DATETIME);
        list.add(TIMESTAMP);
        return list;
    }

    /**
     * @return  数字类型的list
     */
    public List<String> getNumberTypeList(){
        List<String> list= new ArrayList<>();
        list.add(INT);
        list.add(TINYINT);
        list.add(SMALLINT);
        list.add(MEDIUMINT);
        list.add(INTEGER);
        list.add(BIGINT);
        list.add(FLOAT);
        list.add(DECIMAL);
        return list;
    }

    /**
     * @Description 检查是否是字符串类型的数据类型
     * @param fileTypeValue
     * @return 返回true则是字符串类型的数据类型
     */
    public static boolean checkStringType(String fileTypeValue) {
        MySQL mySQL = new MySQL();
        List<String> list = mySQL.getStringTypeList();
        if (list.stream().anyMatch(typeValue -> fileTypeValue.toUpperCase().contains(typeValue.toUpperCase()))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @Description 检查是否是日期类型
     * @param fileTypeValue
     * @return返回true则是字符串类型的数据类型
     */
    public static boolean checkDateType(String fileTypeValue){
        MySQL mySQL = new MySQL();
        List<String> list = mySQL.getDateTypeList();
        if (list.stream().anyMatch(typeValue -> fileTypeValue.toUpperCase().contains(typeValue.toUpperCase()))){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @Description 检查是否是数字类型
     * @param fileTypeValue
     * @return
     */
    public static boolean checkNumberType(String fileTypeValue){
        MySQL mySQL = new MySQL();
        List<String> list = mySQL.getNumberTypeList();
        if (list.stream().anyMatch(typeValue -> fileTypeValue.toUpperCase().contains(typeValue.toUpperCase()))){
            return true;
        }else {
            return false;
        }
    }

}
