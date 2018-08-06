package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈创建索引sql语句，按  表名_IDX序列号 命名〉<br>
 * 〈LeiRui〉
 *
 * @author Shmily
 * @create 2018/8/1
 * @since 1.0.0
 */
public class CreateIndexUtil {
    public static List<StringBuffer> outIndexSQL(List<Table> tables) {
        List<StringBuffer> list = new ArrayList<>();
        for (Table table : tables) {
            List<Field> fieldList = table.getFields();
            StringBuffer createIndexSQL = new StringBuffer();
            int count = fieldList.size();           //计数器，观察是否到了最后一个元素
            int i = 1;
            //拼接SQL
            for (Field field : fieldList) {
                String fieldTableName = table.getTableEnName();/*此处修改为表名*/
                String fieldEnName = field.getFieldEnName();
                String fieldIndexType = field.getIndexType();

                if (!"".equals(field.getIndexNum())) {
                    int fieldIndexNum = Integer.parseInt(field.getIndexNum());
                    i = fieldIndexNum;
                }
                if ("D".equals(fieldIndexType)) {
                    createIndexSQL.append("CREATE INDEX " + fieldTableName).append("_IDX" + i++).append(" ON " + fieldTableName + "(" + fieldEnName + ");"+"\n");
                } else if ("U".equals(fieldIndexType)) {
                    createIndexSQL.append("CREATE UNIQUE INDEX " + fieldTableName).append("_IDX" + i++).append(" ON " + fieldTableName + "(" + fieldEnName + ");"+"\n");
                } else {
                    createIndexSQL.append("  ");
                }
                count--;
            }


            System.out.println("索引sql" + createIndexSQL);
            list.add(createIndexSQL);
        }

        return list;
    }
}