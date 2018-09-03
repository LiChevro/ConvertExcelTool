package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description SQL格式化类
 * @author ll
 * @since 1.0
 */
public class FormatSqlUtil {

    /**
     * @Description 字段名定长35个长度输出
     * @param field
     * @return
     */
    public static String formatField(String field){
        String formatStr = String.format("%-35s",field);
        return formatStr;
    }

    /**
     * @Description 根据字段长度设定字段的最大长度
     * @param table
     * @param fieldEnName
     * @return
     */
    public static String formatField(Table table,String fieldEnName){
        int maxLength = 0;
        List<Field> fieldList = new ArrayList<>();
        fieldList = table.getFields();
        for (Field field:fieldList) {
            if (maxLength < field.getFieldEnName().length()) {
                maxLength = field.getFieldEnName().length();
            }
        }
        int formatLength = maxLength + 2;
        String formatStr = String.format("%-"+formatLength+"s",fieldEnName);
        return formatStr;
    }

    /**
     * @Description 字段类型定长15个长度输出
     * @param fieldType
     * @return
     */
    public static String formatFieldType(String fieldType){
        String formatStr = String.format("%-15s",fieldType);
        return formatStr;
    }

    /**
     * @Description 根据类型长度确定最大长度，用于格式化输出
     * @param table
     * @param fieldType
     * @return
     */
    public static String formatFieldType(Table table,String fieldType){
        int maxLength = 0;
        List<Field> fieldList = new ArrayList<>();
        fieldList = table.getFields();
        for (Field field:fieldList) {
            if (maxLength < field.getFieldType().length()) {
                maxLength = field.getFieldType().length();
            }
        }
        int formatLength = maxLength + 2;
        String formatStr = String.format("%-"+formatLength+"s",fieldType);
        return formatStr;
    }

    public static void main(String[] args) {
        Field field = new Field("    GROUPNAME     ", "    组织名 ", "varchar(10) ", "否", "是", "", "001", "D", "组织名       ");
        Field field1 = new Field("   GROUPID      ", "   组织名 ", "Date    ", "是", "否", "", "001", "D", "组织名       ");
        Field field2 = new Field("   group123     ", "   组织123   ", "INT ", "是", "否", "123       ", "001", "D", "组织名     ");
        List<Field> list = new ArrayList<>();
        list.add(field);
        list.add(field1);
        list.add(field2);
        Table table = new Table();
        table.setFields(list);
        table.setDatabaseBrand("MySQL");
        table.setTableSpace("tablespaceA");
        table.setTableEnName("tableA");
        table.setTableChName("表A");
        String formatStr = formatField(table,"Group");
        System.out.println(formatStr.length());
        System.out.println(formatFieldType(table,"varchar(3)").length());
    }

}
