package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.service.MySQL;
import com.hisuntech.entity.Table;
import com.hisuntech.service.SqlWords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 生成SQL的工具类
 * @author ll
 * @created 2018.7.31
 */
public class GenerateSQLVersion1 {

    private static final String YES = "是";
    private static final String NO = "否";


    /**
     * @Descripton 拼接SQL版本一
     * @param table
     * @return
     */
    public static StringBuffer appendSql(Table table) {
        StringBuffer createSQL = new StringBuffer();
        createSQL.append(SqlWords.CREATE).append("  "+SqlWords.TABLE).append("  " + table.getTableEnName()).append("(" + "\n");
        List<Field> fieldList = table.getFields();
        int count = fieldList.size();                                                                                                   //计数器，观察是否到了最后一个元素
        for (Field field : fieldList) {
            String fieldEnName = field.getFieldEnName().trim();
            String fieldType = field.getFieldType().trim();
            String isNullAble = field.getIsNullAble();
            String defaultValue = field.getDefaultValue().trim();
            createSQL.append("  " + FormatSqlUtil.formatField(table,fieldEnName)).append("  " + FormatSqlUtil.formatFieldType(table,fieldType));
            //设置默认值
            if (defaultValue != "") {
                createSQL.append("  " + SqlWords.DEFAULT);
                if (MySQL.checkStringType(fieldType)){
                    createSQL.append("  "+"'"+defaultValue+"'");
                }else{
                    createSQL.append("  "+defaultValue);
                }
            }else{
                if(YES.equals(isNullAble) && count != 1){                    //处理逗号
                    if (SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase()) && field.getFieldDescription() != ""){
                        createSQL.append("");
                    }else{
                        createSQL.append(",");
                    }
                }
            }
            if (NO.equals(isNullAble)) {
                createSQL.append("  "+SqlWords.NOT_NULL);
                //处理逗号
                if(count != 1 && !SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                    createSQL.append(",");
                }
            }
            //如果数据库是MySQL,则将字段和表的注释加到建表语句的后面
            if (SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                createSQL.append("  "+SqlWords.COMMENT).append("  "+"'").append(field.getFieldDescription()).append("'");
                if (count != 1){
                    createSQL.append(",");
                }
            }
            createSQL.append("\n");
            count--;
        }
        //指定表空间
        if (SqlWords.ORACLE.equals(table.getDatabaseBrand().toUpperCase()) || SqlWords.DB2.equals(table.getDatabaseBrand().toUpperCase())){
            createSQL.append(")").append(table.getTableSpace()).append(";");
            System.out.println("\n建表SQL：" + createSQL);
            return createSQL;
        }else{
            createSQL.append(")");
            if (SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                createSQL.append(SqlWords.COMMENT).append("=").append("'"+table.getTableChName()+"'");
            }
            createSQL.append(";\n");
            System.out.println("\n建表SQL：" + createSQL);
            return createSQL;
        }
    }

    /**
     * @Descripton 输出SQL，包括创建表的SQL与表的注释SQL
     * @param tables
     * @return
     */
    public static Map<String,List> outSql(List<Table> tables) {
        Map<String,List> map = new HashMap<>();
        List<StringBuffer> createSqlList = new ArrayList<>();
        List<StringBuffer> primarySqlList = new ArrayList<>();
        List<StringBuffer> commentSqlList = new ArrayList<>();
        StringBuffer createSQL = new StringBuffer();
        StringBuffer primaryKey = new StringBuffer();
        StringBuffer commentSQL = new StringBuffer();
        for (Table table : tables) {
            createSQL = appendSql(table);
            primaryKey = outSetPrimaryKey(table);
            createSqlList.add(createSQL);
            primarySqlList.add(primaryKey);
            if (table.getDatabaseBrand().toUpperCase().equals(SqlWords.ORACLE) || table.getDatabaseBrand().equals(SqlWords.DB2)){
                commentSQL = outOracleAndDB2CommentSql(table);
                commentSqlList.add(commentSQL);
            }else if (table.getDatabaseBrand().toUpperCase().equals(SqlWords.MYSQL)){
                commentSqlList.add(commentSQL);
            }
        }
        map.put("createSqlList",createSqlList);
        map.put("primarySqlList",primarySqlList);
        map.put("commentSQL",commentSqlList);
        return map;
    }

    /**
     * @Description ORACLE注释SQL与DB2的注释SQL
     * @param table
     * @return
     */
    public static StringBuffer outOracleAndDB2CommentSql(Table table) {
         StringBuffer commentSQL = new StringBuffer();
         List<Field> fieldList = table.getFields();
         commentSQL.append(SqlWords.COMMENT).append("  "+SqlWords.ON+"  ").append(SqlWords.TABLE).append("  " + table.getTableEnName()).append("  " + SqlWords.IS).append("  " + "'" + table.getTableChName() + "'" + ";" + "\n");
         fieldList.stream().forEach(field -> {
             String fieldEnName = field.getFieldEnName();
             String fieldDescription = field.getFieldDescription();
             commentSQL.append(SqlWords.COMMENT).append("  "+SqlWords.ON+"  ").append(SqlWords.COLUMN).append("  " + table.getTableEnName() + ".").append(fieldEnName).append("  " + "IS").append("  " + "'" + fieldDescription + "'" + ";" + "\n");
         });
         System.out.println("\n注释SQL：" + commentSQL);
        return commentSQL;
    }

    /**
     * 设置表的主键,版本一，以Excel表中选的主键为准
     * @param table
     * @return
     */
    public static StringBuffer outSetPrimaryKey(Table table){
        StringBuffer primaryKeySQL = new StringBuffer();
        List<Field> fieldList = table.getFields();
        String tableEnName = table.getTableEnName();
        primaryKeySQL.append(SqlWords.ALTER).append("  "+SqlWords.TABLE+"  ").append(tableEnName).append("  " + SqlWords.ADD).append("  "+SqlWords.CONSTRAINT).append("  "+SqlWords.PREFIX_ALIAS).append(tableEnName.toUpperCase()).append("  "+SqlWords.PRIMARY_KEY).append("  "+"(");
        long count =  fieldList.stream().filter(field -> field.getIsPrimaryKey().equals(YES)).count();               //计数器
        for (Field field:fieldList){
            String isPrimaryKey = field.getIsPrimaryKey();
            if (YES.equals(isPrimaryKey)){
                primaryKeySQL.append(field.getFieldEnName().trim().toLowerCase());
                if (count != 1){
                    primaryKeySQL.append(",");
                }
                count--;
            }
        }
        primaryKeySQL.append(");");
        System.out.println("设置主键:"+primaryKeySQL);
        return primaryKeySQL;
    }


    public static void main(String[] args) {
        Field field = new Field("GRROUPNAME  ", "组织名", "varchar", "否", "是", "", "001", "D", "组织名");
        Field field1 = new Field("GROUPID  ", "组织名", "int", "是", "否", "", "001", "D", "组织名");
        Field field2 = new Field("group123  ", "组织123", "varchar", "是", "否", "0123", "001", "D", "组织名");
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
        List<Table> tableList = new ArrayList<>();
        tableList.add(table);
        outSql(tableList);
    }

}


