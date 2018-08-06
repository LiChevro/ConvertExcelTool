package com.hisuntech.service;

import java.util.ArrayList;
import java.util.List;

public class MySQL {

    //数值类型
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

}
