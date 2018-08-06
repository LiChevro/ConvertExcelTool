package com.hisuntech.entity;

/**
 * @Desciption :字段的实体
 * @Created : 2018.7.25
 */
public class Field {

    private String fieldEnName;     //字段英文名
    private String fieldChName;     //字段中文名
    private String fieldType;           //字段类型
    private String isNullAble;          //字段是否为空（是，否）
    private String isPrimaryKey;    //字段是否是主键（是，否）
    private String defaultValue;    //字段的默认值
    private String indexNum;        //索引编号（数字）
    private String indexType;       //索引类型（D，U）
    private String fieldDescription;    //字段说明

    public Field(String fieldEnName, String fieldChName, String fieldType, String isNullAble, String isPrimaryKey, String defaultValue, String indexNum, String indexType, String fieldDescription) {
        this.fieldEnName = fieldEnName;
        this.fieldChName = fieldChName;
        this.fieldType = fieldType;
        this.isNullAble = isNullAble;
        this.isPrimaryKey = isPrimaryKey;
        this.defaultValue = defaultValue;
        this.indexNum = indexNum;
        this.indexType = indexType;
        this.fieldDescription = fieldDescription;
    }

    public Field() {
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getFieldEnName() {
        return fieldEnName;
    }

    public void setFieldEnName(String fieldEnName) {
        this.fieldEnName = fieldEnName;
    }

    public String getFieldChName() {
        return fieldChName;
    }

    public void setFieldChName(String fieldChName) {
        this.fieldChName = fieldChName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsNullAble() {
        return isNullAble;
    }

    public void setIsNullAble(String isNullAble) {
        this.isNullAble = isNullAble;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldEnName='" + fieldEnName + '\'' +
                ", fieldChName='" + fieldChName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", isNullAble='" + isNullAble + '\'' +
                ", isPrimaryKey='" + isPrimaryKey + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", indexNum='" + indexNum + '\'' +
                ", indexType='" + indexType + '\'' +
                ", fieldDescription='" + fieldDescription + '\'' +
                '}';
    }
}
