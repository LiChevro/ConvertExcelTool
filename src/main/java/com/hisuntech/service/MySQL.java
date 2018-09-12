package com.hisuntech.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ll
 * @since 2018.8.20
 */
public class MySQL {

    //数值类型
    public static final String INT = "INT";
    public static final String TINYINT = "TINYINT";
    public static final String SMALLINT = "SMALLINT";
    public static final String MEDIUMINT = "MEDIUMINT";
    public static final String INTEGER = "INTEGER";
    public static final String BIGINT = "BIGINT";
    public static final String FLOAT = "FLOAT";
    public static final String DECIMAL = "DECIMAL";

    //字符串类型
    public static final String CHAR = "CHAR";
    public static final String VARCHAR = "VARCHAR";
    public static final String TINYBLOB = "TINYBLOB";
    public static final String TINYTEXT = "TINYTEXT";
    public static final String BLOB = "BLOB";
    public static final String TEXT = "TEXT";
    public static final String MEDIUMBLOB = "MEDIUMBLOB";
    public static final String MEDIUMTEXT = "MEDIUMTEXT";
    public static final String LOGNGBLOB = "LOGNGBLOB";
    public static final String LONGTEXT = "LONGTEXT";
    public static final String VARBINARY = "VARBINARY";
    public static final String BINARY = "BINARY";
    public static final String BIT = "BIT";

    //日期类型
    public static final String DATE = "DATE";
    public static final String TIME  = "BINARY";
    public static final String YEAR = "YEAR";
    public static final String DATETIME = "DATETIME";
    public static final String TIMESTAMP = "TIMESTAMP";

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
        list.add(BIT);
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
