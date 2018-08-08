package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈创建索引sql语句，按  表名_字段_IDX序列号 命名  D,U,组合索引都可以生成，无索引类型的不生成索引〉<br>
 * 〈LeiRui〉
 *
 * @author Shmily
 * @create 2018/8/7
 * @since 2.0.0
 */
public class CreateIndexUtil {
    static StringBuffer createIndexSQL = new StringBuffer();
    static List<StringBuffer> list = new ArrayList<>();

    public static List<StringBuffer> outIndexSQL(List<Table> tables) {
        String fieldEnName, fieldIndexNum, fieldIndexType = "";
        //遍历每一张表
        for (Table table : tables) {
            List<Field> fieldList = table.getFields();
            createIndexSQL = new StringBuffer("");
            int count = fieldList.size(), k = fieldList.size();           //计数器，观察是否到了最后一个元素
            String[] strs1 = new String[count];
            String[] strs2 = new String[count];
            String[] strs3 = new String[count];
            //将字段英文名，索引编号，索引类型，存放在数组中，方便使用，给没有索引编号的自动生成数字
            for (Field field : fieldList) {
                fieldEnName = field.getFieldEnName();
                fieldIndexNum = field.getIndexNum();
                fieldIndexType = field.getIndexType();
                strs1[count - 1] = fieldIndexNum;//{2,2,3,5,2,5,8}
                strs2[count - 1] = fieldEnName;//{a,b,c,d,e,f,g}
                strs3[count - 1] = fieldIndexType;//{"D","U","","","D","U","U"}
                if ("".equals(strs1[count - 1])) {
                    strs1[count - 1] = String.valueOf(k++);
                }
                count--;
            }
            //动态拼接SQL，如果索引编号相同的话，将相同的行生成组合索引
            String tableName = table.getTableEnName();
            //System.out.println("table.getTableEnName():  " + tableName);
            for (int i = 0; i < strs1.length; i++) {
                String h = strs1[i];
                String name = strs2[i];
                String type = strs3[i];
                boolean repeated = false;
                for (int j = 0; j < i; j++) {
                    if (h.equals(strs1[j])) {
                        repeated = true;
                        break;
                    }
                }
                if (!repeated) {
                    StringBuffer sb = new StringBuffer();
                    for (int j = 0; j < strs1.length; j++) {
                        if (h.equals(strs1[j])) {
                            sb.append(",").append(strs2[j]);
                        }
                    }
                    sb.delete(0, 1);
                    if ("D".equals(type.toUpperCase())) {
                        createIndexSQL.append("CREATE INDEX " + tableName + "_" + name).append("_IDX" + h).append(" ON " + tableName + "(" + sb.toString() + ");\n");
                    } else if ("U".equals(type.toUpperCase())) {
                        createIndexSQL.append("CREATE UNIQUE INDEX " + tableName + "_" + name).append("_IDX" + h).append(" ON " + tableName + "(" + sb.toString() + ");\n");
                    } else {
                        createIndexSQL.append("");
                    }
                }

            }
            System.out.println("索引sql:    " + createIndexSQL);
            list.add(createIndexSQL);
        }
        return list;
    }
    //测试
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
        Table table1 = new Table();
        table1.setFields(list);
        table1.setDatabaseBrand("MySQL");
        table1.setTableSpace("tablespaceA");
        table1.setTableEnName("tableA");
        table1.setTableChName("表A");
        List<Table> tableList = new ArrayList<>();
        tableList.add(table);
        tableList.add(table1);
        outIndexSQL(tableList);
    }

}
