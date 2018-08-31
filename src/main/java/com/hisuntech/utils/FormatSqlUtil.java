package com.hisuntech.utils;

/**
 * @Description SQL格式化类
 * @author ll
 * @since 1.0
 */
public class FormatSqlUtil {

    /**
     * @Description 字段名定长输出
     * @param field
     * @return
     */
    public static String formatField(String field){
        String formatStr = String.format("%-35s",field);
        return formatStr;
    }

    /**
     * @Description 字段类型定长输出
     * @param fieldType
     * @return
     */
    public static String formatFieldType(String fieldType){
        String formatStr = String.format("%-20s",fieldType);
        return formatStr;
    }

    public static void main(String[] args) {
        String string = "abcfd";
        System.out.println(formatField(string).length());
    }

}
