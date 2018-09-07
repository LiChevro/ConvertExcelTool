package com.hisuntech.entity;

import java.util.List;

/**
 * @author  ll
 * @Description 索引
 */
public class Index {

    private String tableEnName;
    private String fieldEnName;
    private List<String> indexNum;
    private List<String> indexType;

    public Index(String tableEnName, String fieldEnName, List<String> indexNum, List<String> indexType) {
        this.tableEnName = tableEnName;
        this.fieldEnName = fieldEnName;
        this.indexNum = indexNum;
        this.indexType = indexType;
    }

    public Index() {
    }

    public String getTableEnName() {
        return tableEnName;
    }

    public void setTableEnName(String tableEnName) {
        this.tableEnName = tableEnName;
    }

    public String getFieldEnName() {
        return fieldEnName;
    }

    public void setFieldEnName(String fieldEnName) {
        this.fieldEnName = fieldEnName;
    }

    public List<String> getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(List<String> indexNum) {
        this.indexNum = indexNum;
    }

    public List<String> getIndexType() {
        return indexType;
    }

    public void setIndexType(List<String> indexType) {
        this.indexType = indexType;
    }

    @Override
    public String toString() {
        return "\nIndex{" +"\n"+
                "tableEnName='" + tableEnName + '\'' +"\n"+
                "fieldEnName='" + fieldEnName + '\'' +"\n"+
                "indexNum=" + indexNum +"\n"+
                "indexType=" + indexType +"\n"+
                '}';
    }
}
