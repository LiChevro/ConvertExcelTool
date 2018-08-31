package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;
import com.hisuntech.service.MySQL;
import com.hisuntech.service.SqlWords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 *  SQL拼接版本三，取消ID，取消索引，以Excel表中的键作为主键，保留create_time，update_time，update_by等字段
 *  create_time,update_time,update_by这三个字段的类型统一设置为datetime
 * @author  ll
 * @created 2018.8.30
 */
public class GenerateSQLVersion3 {

    private static final String YES = "是";
    private static final String NO = "否";

    /**
     * @Description
     * 拼接SQL版本三，自动生成create_time，update_time，update_by等字段，
     * 对于可以为空的字段设置not  null加默认值，字符类型设为空，数字类型设为0，日期类型设为当前的时间
     * 注意：只有ID才能作为主键，其他字段如果在Excel中被设置了主键，依旧当作普通的字段
     * @param table
     * @return
     */
    public static StringBuffer appendSql(Table table){
        StringBuffer createSQL = new StringBuffer();
        createSQL.append(SqlWords.CREATE).append("  "+SqlWords.TABLE).append("  " + table.getTableEnName()).append("(" + "\n");
        List<Field> fieldList = table.getFields();

        for (Field field:fieldList){
            String filedEnName = field.getFieldEnName().trim().toLowerCase();       //字段名全部小写，并去掉两端空格
            String fieldType = field.getFieldType().trim();
            String isNullAble = field.getIsNullAble();
            String filedDescription = field.getFieldDescription().trim();
            String defaultValue = field.getDefaultValue().trim();
            createSQL.append("\t").append(FormatSqlUtil.formatField(filedEnName)).append(FormatSqlUtil.formatFieldType(fieldType));
            createSQL.append("  "+SqlWords.NOT_NULL);
            //根据是否允许为空，以及字段类型设置默认值
            if (YES.equals(isNullAble)){
                createSQL.append("  "+SqlWords.DEFAULT);
                if (!defaultValue.equals("")){
                    if (MySQL.checkStringType(fieldType)){
                        createSQL.append("  "+"'"+defaultValue+"'");
                    }else if (MySQL.checkNumberType(fieldType)){
                        createSQL.append("  "+defaultValue);
                    }else{
                        createSQL.append("  "+defaultValue);
                    }
                }else{
                    if (MySQL.checkStringType(fieldType)){
                        createSQL.append("  "+"' '");
                    }else if (MySQL.checkNumberType(fieldType)){
                        createSQL.append("  "+"0");
                    }else if (MySQL.checkDateType(fieldType)){
                        createSQL.append("  "+SqlWords.CURRENT_TIMESTAMP);
                    }
                }
            }
            if (NO.equals(isNullAble)){
                if (defaultValue != ""){
                    createSQL.append("  "+SqlWords.DEFAULT);
                    if (MySQL.checkStringType(fieldType)){
                        createSQL.append("  "+"'"+defaultValue+"'");
                    }else{
                        createSQL.append("  "+defaultValue);
                    }
                }
            }
            //如果数据库是MySQL,则将字段和表的注释加到建表语句的后面
            if (SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
                createSQL.append("  "+SqlWords.COMMENT).append("  "+"'").append(filedDescription).append("'");
            }
            createSQL.append(","+"\n");
        }
        createSQL.append("\t").append(FormatSqlUtil.formatField(SqlWords.create_time)).append(FormatSqlUtil.formatFieldType("DATETIME")).append("  "+SqlWords.NOT_NULL).append("  "+SqlWords.DEFAULT).append("  "+SqlWords.CURRENT_TIMESTAMP)
                .append("  "+SqlWords.COMMENT).append("  "+"'创建时间'").append(","+"\n");
        createSQL.append("\t").append(FormatSqlUtil.formatField(SqlWords.update_time)).append(FormatSqlUtil.formatFieldType("DATETIME")).append("  "+SqlWords.NOT_NULL).append("  "+SqlWords.DEFAULT).append("  "+SqlWords.CURRENT_TIMESTAMP)
                .append("  "+SqlWords.ON_UPDATE).append("  "+SqlWords.CURRENT_TIMESTAMP).append("  "+SqlWords.COMMENT).append("  "+"'最近更新日期时间'").append(","+"\n");
        createSQL.append("\t").append(FormatSqlUtil.formatField(SqlWords.update_by)).append(FormatSqlUtil.formatFieldType("VARCHAR(10)")).append("  "+SqlWords.NOT_NULL).append("  "+SqlWords.COMMENT).append("  "+"'修改人'").append("\n");
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
     * 设置表的主键,版本三，以Excel表中选的主键为准
     * @param table
     * @return
     */
    public static StringBuffer outSetPrimaryKey(Table table){
        StringBuffer primaryKeySQL = new StringBuffer();
        String tableEnName = table.getTableEnName();
        List<Field> fieldList = table.getFields();
        primaryKeySQL.append(SqlWords.ALTER).append("  "+SqlWords.TABLE+"  ").append(tableEnName).append("  " + SqlWords.ADD).append("  "+SqlWords.CONSTRAINT).append("  "+SqlWords.PREFIX_ALIAS).append(tableEnName.toUpperCase()).append("  "+SqlWords.PRIMARY_KEY).append("  "+"(");
        long count =  fieldList.stream().filter(field -> field.getIsPrimaryKey().equals(YES)).count();               //计数器
        for (Field field:fieldList){
            String isPrimaryKey = field.getIsPrimaryKey();
            String FieldEnName = field.getFieldEnName().trim();
            if (YES.equals(isPrimaryKey)){
                primaryKeySQL.append(FieldEnName.toLowerCase());
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
     * @Description 输出SQL语句
     * @param tables
     * @return Map
     */
    public static Map<String,List> outSql(List<Table> tables){
        Map<String,List> map = new HashMap<>();
        List<StringBuffer> createSqlList = new ArrayList<>();
        List<StringBuffer> commentSqlList = new ArrayList<>();
        StringBuffer createSQL = new StringBuffer();
        StringBuffer commentSQL = new StringBuffer();
        StringBuffer primaryKey = new StringBuffer();
        for (Table table : tables) {
            createSQL = appendSql(table);
            primaryKey = outSetPrimaryKey(table);
            createSQL.append("\n"+primaryKey);
            createSqlList.add(createSQL);
            if (table.getDatabaseBrand().toUpperCase().equals(SqlWords.ORACLE) || table.getDatabaseBrand().equals(SqlWords.DB2)){
                commentSQL = GenerateSQLVersion1.outOracleAndDB2CommentSql(table);
                commentSqlList.add(commentSQL);
            }else if (table.getDatabaseBrand().toUpperCase().equals(SqlWords.MYSQL)){
                commentSqlList.add(commentSQL);
            }
        }
        map.put("createSqlList",createSqlList);
        map.put("commentSQL",commentSqlList);
        return map;
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
        List<Table> tableList = new ArrayList<>();
        tableList.add(table);
        outSql(tableList);
    }
}
