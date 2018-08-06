package com.hisuntech.entity;

import java.util.List;

/**
 * @Desciption :表的实体
 * @Created : 2018.7.25
 */
public class Table {

    private String tableChName;     //表中文名
    private String tableEnName;     //表英文名

    private List<Field> fields;         //表中的字段

    private String databaseName;              //数据库名
    private String databaseBrand;              //数据库商（oracle,mysql,db2,sql server）
    private String tableSpace;                      //表空间

    public Table(String tableChName, String tableEnName, List<Field> fields, String databaseName, String databaseBrand) {
        this.tableChName = tableChName;
        this.tableEnName = tableEnName;
        this.fields = fields;
        this.databaseName = databaseName;
        this.databaseBrand = databaseBrand;
    }

    public Table() {
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseBrand() {
        return databaseBrand;
    }

    public void setDatabaseBrand(String databaseBrand) {
        this.databaseBrand = databaseBrand;
    }

    public String getTableChName() {
        return tableChName;
    }

    public void setTableChName(String tableChName) {
        this.tableChName = tableChName;
    }

    public String getTableEnName() {
        return tableEnName;
    }

    public void setTableEnName(String tableEnName) {
        this.tableEnName = tableEnName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getTableSpace() {
        return tableSpace;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace = tableSpace;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableChName='" + tableChName + '\'' +
                ", tableEnName='" + tableEnName + '\'' +
                ", fields=" + fields +
                ", databaseName='" + databaseName + '\'' +
                ", databaseBrand='" + databaseBrand + '\'' +
                ", tableSpace='" + tableSpace + '\'' +
                '}';
    }
}
