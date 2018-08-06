package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 生成SQL的工具类
 * @created 2018.7.31
 */
public class GenerateSqlUtil {

    private static final String YES = "是";
    private static final String NO = "否";

    /**
     * 拼接SQL
     * @param table
     * @return
     */
    public static StringBuffer appendSql(Table table){
        StringBuffer createSQL = new StringBuffer();
        createSQL.append("CREATE  TABLE").append("  "+table.getTableEnName()).append("("+"\n");
        List<Field> fieldList = table.getFields();
        int count = fieldList.size();           //计数器，观察是否到了最后一个元素
        for (Field field: fieldList) {
            String fieldEnName = field.getFieldEnName();
            String fieldType = field.getFieldType();
            String isNullAble = field.getIsNullAble();
            String isPrimaryKey = field.getIsPrimaryKey();
            String defaultValue = field.getDefaultValue();
            createSQL.append("  "+fieldEnName).append("  "+fieldType);
            if (defaultValue != ""){
                createSQL.append("  "+"DEFAULT");

            }
            if (YES.equals(isPrimaryKey)){
                createSQL.append("  "+"PRIMARY KEY PK_"+fieldEnName);
                if (YES.equals(isNullAble) && count == 1){
                    createSQL.append(",");
                }
            }
            if (NO.equals(isNullAble)){
                if (count == 1){
                    createSQL.append("  "+"NOT NULL");
                }else{
                    createSQL.append("  "+"NOT NULL,");
                }
            }else if (NO.equals(isNullAble) && count == 1){
                createSQL.append("");
            }
            createSQL.append("\n");
            count--;
        }
        createSQL.append(");\n");
        System.out.println("\n建表SQL："+createSQL);
        return createSQL;
    }

    /**
     * 创表SQL
     * @param tables
     * @return
     */
    public static List<StringBuffer> outCreatedSql(List<Table> tables){
        List<StringBuffer> list = new ArrayList<>();
        StringBuffer createSQL = new StringBuffer();
        for (Table table:tables){
            if ("ORACLE".toUpperCase().equals(table.getDatabaseBrand().toUpperCase())){
                createSQL = appendSql(table);
            }else if ("DB2".toUpperCase().equals(table.getDatabaseBrand().toUpperCase())){
                createSQL = appendSql(table);
            }else if ("MySQL".toUpperCase().equals(table.getDatabaseBrand().toUpperCase())){
                createSQL = appendSql(table);
            }
            list.add(createSQL);
        }
        return list;
    }

    /**
     * 注释SQL
     * @param tables
     * @return
     */
    public static List<StringBuffer> outCommentSql(List<Table> tables){
        List<StringBuffer> list = new ArrayList<>();
        for(Table table:tables){
            StringBuffer commentSQL = new StringBuffer();
            List<Field> fieldList = table.getFields();
            commentSQL.append("COMMENT ON TABLE").append("  "+table.getTableEnName()).append("  "+"IS").append("  "+"'"+table.getTableChName()+"'"+";"+"\n");
            fieldList.stream().forEach(field ->{
                String fieldEnName = field.getFieldEnName();
                String fieldDescription = field.getFieldDescription();
                commentSQL.append("COMMENT ON COLUMN").append("  "+table.getTableEnName()+".").append(fieldEnName).append("  "+"IS").append("  "+"'"+fieldDescription+"'"+";"+"\n");
            });
            System.out.println("\n注释SQL："+commentSQL);
            list.add(commentSQL);
        }
        return list;
    }

    /**
     * 表空间
     * @param tables
     * @return
     */
    public static List<StringBuffer> outTableSpace(List<Table> tables){
        List<StringBuffer> list = new ArrayList<>();
        for (Table table:tables){
            StringBuffer tableSpaceSQL = new StringBuffer();
            tableSpaceSQL.append(table.getTableSpace()+";");
            list.add(tableSpaceSQL);
        }
        return list;
    }


}
