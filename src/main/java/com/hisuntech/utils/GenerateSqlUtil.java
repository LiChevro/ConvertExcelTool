package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.service.MySQL;
import com.hisuntech.entity.Table;
import groovy.util.IFileNameFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 生成SQL的工具类
 * @author ll
 * @created 2018.7.31
 */
public class GenerateSqlUtil {

    private static final String YES = "是";
    private static final String NO = "否";

    private static final String CREATE = "CREATE";
    private static final String TABLE = "TABLE";
    private static final String DEFAULT = "DEFAULT";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String PREFIX_ALIAS = "PK_";
    private static final String NOT_NULL = "NOT NULL";
    
    private static final String ORACLE = "ORACLE";
    private static final String DB2 = "DB2";
    private static final String MYSQL = "MYSQL";

    private static final String COMMENT = "COMMENT";
    private static final String ON = "ON";
    private static final String COLUMN = "COLUMN";
    private static final String IS = "IS";
    private static final String ALTER = "ALTER";
    private static final String MODIFY = "MODIFY";
    private static final String CONSTRAINT = "CONSTRAINT";
    private static final String ADD = "ADD";


    /**
     * @Descripton 拼接SQL
     * @param table
     * @return
     */
    public static StringBuffer appendSql(Table table) {
        StringBuffer createSQL = new StringBuffer();
        createSQL.append(CREATE).append("  "+TABLE).append("  " + table.getTableEnName()).append("(" + "\n");
        List<Field> fieldList = table.getFields();
        int count = fieldList.size();                                                                                                   //计数器，观察是否到了最后一个元素
        for (Field field : fieldList) {
            String fieldEnName = field.getFieldEnName();
            String fieldType = field.getFieldType();
            String isNullAble = field.getIsNullAble();
            String defaultValue = field.getDefaultValue();
            createSQL.append("  " + fieldEnName).append("  " + fieldType);
            if (defaultValue != "") {
                createSQL.append("  " + DEFAULT);
                if (checkStringType(fieldType)){
                    createSQL.append("  "+"'"+defaultValue+"'");
                }else{
                    createSQL.append("  "+defaultValue);
                }
                if(YES.equals(isNullAble) && count != 1){                    //处理逗号
                    createSQL.append(",");
                }
            }else{
                if(YES.equals(isNullAble) && count != 1){                    //处理逗号
                    if (MYSQL.equals(table.getDatabaseBrand().toUpperCase()) && field.getFieldDescription() != ""){
                        createSQL.append("");
                    }else{
                        createSQL.append(",");
                    }

                }
            }
            if (NO.equals(isNullAble)) {
                createSQL.append("  "+NOT_NULL);
                if(count != 1 && !MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                    createSQL.append(",");
                }
      /*          if (count == 1) {
                    createSQL.append("  " + NOT_NULL);
                } else {
                    createSQL.append("  " + NOT_NULL + ",");
                }*/
            }
            //如果数据库是MySQL,则将字段和表的注释加到建表语句的后面
            if (MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                createSQL.append("  "+COMMENT).append("  "+"'").append(field.getFieldDescription()).append("'");
                if (count != 1){
                    createSQL.append(",");
                }
            }
            createSQL.append("\n");
            count--;
        }
        //指定表空间
        if (ORACLE.equals(table.getDatabaseBrand().toUpperCase()) || DB2.equals(table.getDatabaseBrand().toUpperCase())){
            createSQL.append(")").append(table.getTableSpace()).append(";");
            System.out.println("\n建表SQL：" + createSQL);
            return createSQL;
        }else{
            createSQL.append(")");
            if (MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                createSQL.append(COMMENT).append("=").append("'"+table.getTableChName()+"'");
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
            if (table.getDatabaseBrand().toUpperCase().equals(ORACLE) || table.getDatabaseBrand().equals(DB2)){
                commentSQL = outOracleAndDB2CommentSql(table);
                commentSqlList.add(commentSQL);
            }else if (table.getDatabaseBrand().toUpperCase().equals(MYSQL)){
//                commentSQL = outMySQLCommentSql(table);
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
         commentSQL.append(COMMENT).append("  "+ON+"  ").append(TABLE).append("  " + table.getTableEnName()).append("  " + IS).append("  " + "'" + table.getTableChName() + "'" + ";" + "\n");
         fieldList.stream().forEach(field -> {
             String fieldEnName = field.getFieldEnName();
             String fieldDescription = field.getFieldDescription();
             commentSQL.append(COMMENT).append("  "+ON+"  ").append(COLUMN).append("  " + table.getTableEnName() + ".").append(fieldEnName).append("  " + "IS").append("  " + "'" + fieldDescription + "'" + ";" + "\n");
         });
         System.out.println("\n注释SQL：" + commentSQL);
        return commentSQL;
    }

    /**
     * @Description MySQL的注释SQL
     * @param table
     * @return
     */
/*    public static StringBuffer outMySQLCommentSql(Table table){
        StringBuffer commentSQL = new StringBuffer();
        List<Field> fieldList = table.getFields();
        commentSQL.append(ALTER).append("  "+TABLE+"  ").append(table.getTableEnName()).append("  "+COMMENT).append("  "+"'" + table.getTableChName() + "'" + ";" + "\n");
        fieldList.stream().forEach(field -> {
            String fieldEnName = field.getFieldEnName();
            String fieldDescription = field.getFieldDescription();
            String fieldType = field.getFieldType();
            commentSQL.append(ALTER).append("  "+TABLE+"  ").append(table.getTableEnName()).append("  "+MODIFY).append("  "+COLUMN+"  ").append(fieldEnName).append("  "+fieldType).append("  "+COMMENT).append("  " + "'" + fieldDescription + "'" + ";" + "\n");
        });
        System.out.println("\n注释SQL：" + commentSQL);
        return commentSQL;
    }*/

/*    *//**
     * @Description 指定表空间的SQL
     * @param tables
     * @return
     *//*
    public static List<StringBuffer> outTableSpace(List<Table> tables) {
        List<StringBuffer> list = new ArrayList<>();
        for (Table table : tables) {
            StringBuffer tableSpaceSQL = new StringBuffer();
            tableSpaceSQL.append(table.getTableSpace() + ";");
            list.add(tableSpaceSQL);
        }
        return list;
    }*/

    /**
     * 设置表的主键
     * @param table
     * @return
     */
    public static StringBuffer outSetPrimaryKey(Table table){
        StringBuffer primaryKeySQL = new StringBuffer();
        List<Field> fieldList = table.getFields();
        primaryKeySQL.append(ALTER).append("  "+TABLE+"  ").append(table.getTableEnName()).append("  " + ADD).append("  "+CONSTRAINT).append("  "+PREFIX_ALIAS).append(table.getTableEnName()).append("  "+PRIMARY_KEY).append("  "+"(");
        long count =  fieldList.stream().filter(field -> field.getIsPrimaryKey().equals(YES)).count();               //计数器
        for (Field field:fieldList){
            String isPrimaryKey = field.getIsPrimaryKey();
            if (YES.equals(isPrimaryKey)){
                primaryKeySQL.append(field.getFieldEnName());
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

    /**
     * @Description 检查是否是字符串类型的数据类型
     * @param defaultValue
     * @return 返回true则是字符串类型的数据类型
     */
    public static boolean checkStringType(String defaultValue) {
        MySQL mySQL = new MySQL();
        List<String> list = mySQL.getStringTypeList();
        if (list.stream().anyMatch(typeValue -> defaultValue.toUpperCase().contains(typeValue.toUpperCase()))){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        Field field = new Field("groupName", "组织名", "varchar", "是", "是", "", "001", "D", "组织名");
        Field field1 = new Field("groupid", "组织名", "int", "是", "否", "", "001", "D", "组织名");
        Field field2 = new Field("group123", "组织123", "varchar", "是", "否", "0123", "001", "D", "组织名");
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


