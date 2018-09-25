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
            createSQL.append("\t").append(FormatSqlUtil.formatField(table,filedEnName)).append(fieldType.toUpperCase());
            createSQL.append(","+"\n");
        }
        createSQL.append("\t").append(FormatSqlUtil.formatField(table,SqlWords.create_time)).append("DATETIME").append(",").append("\n");
        createSQL.append("\t").append(FormatSqlUtil.formatField(table,SqlWords.modify_time)).append("DATETIME").append(",").append("\n");
        createSQL.append("\t").append(FormatSqlUtil.formatField(table,SqlWords.update_by)).append("VARCHAR(10)").append("\n");
        //指定表空间
        if (SqlWords.ORACLE.equals(table.getDatabaseBrand().toUpperCase()) || SqlWords.DB2.equals(table.getDatabaseBrand().toUpperCase())){
            createSQL.append(")").append(table.getTableSpace()).append(";");
            System.out.println("\n建表SQL：" + createSQL);
            return createSQL;
        }else{
            createSQL.append(")");
//            if (SqlWords.MYSQL.equals(table.getDatabaseBrand().toUpperCase())){
//                createSQL.append(SqlWords.COMMENT).append("=").append("'"+table.getTableChName()+"'");
//            }
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
     * @Description 输出MySQL的注释
     * @param table
     * @return
     */
    public static StringBuffer outMySQLComment(Table table){
        StringBuffer commentSQL = new StringBuffer();
        String tableChName = table.getTableChName().trim();
        String tableEnName = table.getTableEnName().trim().toLowerCase();   //英文表名，去掉前后空格，并且小写
        List<Field> fieldList = table.getFields();
        commentSQL.append("\n");
        //表的注释
        commentSQL.append(SqlWords.ALTER).append("  ")
                .append(SqlWords.TABLE).append("  ").append(tableEnName).append("  ")
                .append(SqlWords.COMMENT).append("  ")
                .append("'").append(tableChName).append("'").append(";");
        //字段的注释
        for (Field field:fieldList){
            String fileEnName = field.getFieldEnName().trim().toLowerCase();        //字段英文名去掉空格小写
            String fieldType = field.getFieldType().trim().toUpperCase();       //类型去掉空格大写
            String fieldDescription = field.getFieldDescription().trim();
            String isNullAble = field.getIsNullAble().trim();
            String defaultValue = field.getDefaultValue().trim();
            commentSQL.append("\n");
            commentSQL.append(SqlWords.ALTER).append("  ").append(SqlWords.TABLE).append("  ").append(tableEnName).append("  ")
                    .append(SqlWords.MODIFY).append("  ").append(SqlWords.COLUMN).append("  ").append(FormatSqlUtil.formatField(table,fileEnName)).append(FormatSqlUtil.formatFieldType(table,fieldType));
            //添加表的其他约束
            commentSQL.append(SqlWords.NOT_NULL);
            //根据是否允许为空以及字段类型设置默认值
            if (YES.equals(isNullAble)){
                commentSQL.append("  ");
                commentSQL.append(SqlWords.DEFAULT);
                if (!defaultValue.equals("")){
                    if (MySQL.checkStringType(fieldType)){
                        commentSQL.append("  "+"'"+defaultValue+"'");
                    }else if (MySQL.checkNumberType(fieldType)){
                        commentSQL.append("  "+defaultValue);
                    }else{
                        commentSQL.append("  "+defaultValue);
                    }
                    commentSQL.append("  ");
                }else{
                    if (MySQL.checkStringType(fieldType)){
                        commentSQL.append("  "+"' '");
                    }else if (MySQL.checkNumberType(fieldType)){
                        commentSQL.append("  "+"0");
                    }else if (MySQL.checkDateType(fieldType)){
                        commentSQL.append("  "+SqlWords.CURRENT_TIMESTAMP);
                    }
                    commentSQL.append("  ");
                }
            }
            if (NO.equals(isNullAble)){
                if (defaultValue != ""){
                    commentSQL.append("  "+SqlWords.DEFAULT);
                    if (MySQL.checkStringType(fieldType)){
                        commentSQL.append("  "+"'"+defaultValue+"'");
                    }else{
                        commentSQL.append("  "+defaultValue);
                    }
                }
                commentSQL.append("  ");
            }
            commentSQL.append(SqlWords.COMMENT).append("  ").append("'").append(fieldDescription).append("'").append(";");
        }
        //拼接固定的字段的注释
        commentSQL.append("\n");
        commentSQL.append(SqlWords.ALTER).append("  ").append(SqlWords.TABLE).append("  ").append(tableEnName).append("  ")
                .append(SqlWords.MODIFY).append("  ").append(SqlWords.COLUMN).append("  ").append(FormatSqlUtil.formatField(table,SqlWords.create_time)).append(FormatSqlUtil.formatFieldType(table,"DATETIME")).append(SqlWords.NOT_NULL).append("  ")
                .append(SqlWords.COMMENT).append("  ").append("'").append("创建时间").append("'").append(";").append("\n");
        commentSQL.append(SqlWords.ALTER).append("  ").append(SqlWords.TABLE).append("  ").append(tableEnName).append("  ")
                .append(SqlWords.MODIFY).append("  ").append(SqlWords.COLUMN).append("  ").append(FormatSqlUtil.formatField(table,SqlWords.modify_time)).append(FormatSqlUtil.formatFieldType(table,"DATETIME")).append(SqlWords.NOT_NULL).append("  ")
                .append(SqlWords.COMMENT).append("  ").append("'").append("更新时间").append("'").append(";").append("\n");
        commentSQL.append(SqlWords.ALTER).append("  ").append(SqlWords.TABLE).append("  ").append(tableEnName).append("  ")
                .append(SqlWords.MODIFY).append("  ").append(SqlWords.COLUMN).append("  ").append(FormatSqlUtil.formatField(table,SqlWords.update_by)).append(FormatSqlUtil.formatFieldType(table,"VARCHAR(10)")).append(SqlWords.NOT_NULL).append("  ")
                .append(SqlWords.COMMENT).append("  ").append("'").append("修改人").append("'").append(";").append("\n");
        System.out.println("注释SQL:\n"+commentSQL);
        return commentSQL;
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
                commentSQL = GenerateSQLVersion3.outMySQLComment(table);
                commentSqlList.add(commentSQL);
            }
        }
        map.put("createSqlList",createSqlList);
        map.put("commentSQL",commentSqlList);
        return map;
    }

    public static void main(String[] args) {
        Field field = new Field("    GROUPNAME     ", "    组织名 ", "varchar(10) ", "否", "是", "1408030340", "001", "D", "组织名       ");
        Field field1 = new Field("   GROUPID      ", "   组织名 ", "varchar(10) ", "是", "否", "", "001", "D", "组织名       ");
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
