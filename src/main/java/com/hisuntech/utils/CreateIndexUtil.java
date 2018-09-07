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

    public static List<StringBuffer> outIndexSQL(List<Table> tables, String fieldTableType) {
        String fieldEnName, fieldIndexNum, isPrimaryKey = "", fieldIndexType = "";
        //遍历每一张表
        for (Table table : tables) {
            List<Field> fieldList = table.getFields();
            createIndexSQL = new StringBuffer("");
            int count = fieldList.size(), k = fieldList.size();           //计数器，观察是否到了最后一个元素
            //索引编号数组
            String[] fieldIndexNums = new String[count];
            //字段英文名数组
            String[] fieldEnNames = new String[count];
            //索引类型数组
            String[] fieldIndexTypes = new String[count];
            //将字段英文名，索引编号，索引类型，存放在数组中，方便使用，给没有索引编号的自动生成数字
            for (Field field : fieldList) {
                fieldEnName = field.getFieldEnName();
                fieldIndexNum = field.getIndexNum();
                fieldIndexType = field.getIndexType();
                isPrimaryKey = field.getIsPrimaryKey();
                fieldIndexNums[count - 1] = fieldIndexNum;//{2,2,3,5,2,5,8}
                fieldEnNames[count - 1] = fieldEnName;//{a,b,c,d,e,f,g}
                fieldIndexTypes[count - 1] = fieldIndexType;//{"D","U","","","D","U","U"}
                if ("".equals(fieldIndexNums[count - 1])) {
                    fieldIndexNums[count - 1] = String.valueOf(k++);
                }
                if ("2".equalsIgnoreCase(fieldTableType)) {
                    if ("是".equals(isPrimaryKey)) {
                        fieldIndexTypes[count - 1] = "U";
                        System.out.println("fieldIndexTypes[count - 1]:  " + fieldIndexTypes[count - 1]);
                    }
                }
                count--;
            }
            //对索引编号进行排序
            List  indexlist = new ArrayList();
            indexlist = sortIndex(fieldIndexNums,fieldIndexTypes);
            fieldIndexNums = (String[]) indexlist.get(0);
            fieldIndexTypes = (String[]) indexlist.get(1);
            //动态拼接SQL，如果索引编号相同的话，将相同的行生成组合索引
            String tableName = table.getTableEnName();
            //System.out.println("table.getTableEnName():  " + tableName);
//            if ("2".equalsIgnoreCase(fieldTableType)) {
//                createIndexSQL.append("CREATE UNIQUE INDEX " + tableName).append("_IDX0").append(" ON " + tableName + "(id);\n");
//            }
            for (int i = 0; i < fieldIndexNums.length; i++) {
                String h = fieldIndexNums[i];
                String type = fieldIndexTypes[i];
                boolean repeated = false;
                for (int j = 0; j < i; j++) {
                    if (h.equals(fieldIndexNums[j])) {
                        repeated = true;
                        break;
                    }
                }
                if (!repeated) {
                    StringBuffer sb = new StringBuffer();
                    for (int j = 0; j < fieldIndexNums.length; j++) {
                        if (h.equals(fieldIndexNums[j])) {
                            sb.append(",").append(fieldEnNames[j]);
                        }
                    }
                    sb.delete(0, 1);

                    if ("D".equals(type.toUpperCase())) {
                        createIndexSQL.append("CREATE INDEX " + tableName).append("_IDX" + h).append(" ON " + tableName + "(" + sb.toString() + ");\n");
                    } else if ("U".equals(type.toUpperCase())) {
                        createIndexSQL.append("CREATE UNIQUE INDEX " + tableName).append("_IDX" + h).append(" ON " + tableName + "(" + sb.toString() + ");\n");
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


    /**
     * 对索引编号进行排序
     * @author ll
     * @param indexNum
     * @param indexType
     * @return
     */
    public static List sortIndex(String[] indexNum,String[] indexType){
        Integer temp = 0;
        String tempStr = null;
        for (int i=0; i<indexNum.length-1; i++){
            for (int j=indexNum.length - i- 1; j>0; j--){
                Integer leftValue = Integer.parseInt(indexNum[j-1]);
                Integer rightValue = Integer.parseInt(indexNum[j]);
                if (leftValue > rightValue){
                    temp = leftValue;
                    leftValue = rightValue;
                    rightValue = temp;
                    indexNum[j-1] = leftValue + "";
                    indexNum[j] = rightValue + "";
                    tempStr = indexType[j-1];
                    indexType[j-1] = indexType[j];
                    indexType[j] = tempStr;
                }
            }
        }
        List  list = new ArrayList();
        list.add(indexNum);
        list.add(indexType);
        return list;
    }

    //测试
    public static void main(String[] args) {
        Field field = new Field("groupName", "组织名", "varchar", "是", "是", "", "2", "D", "组织名");
        Field field1 = new Field("groupid", "组织名", "int", "是", "否", "", "3", "D", "组织名");
        Field field2 = new Field("group123", "组织123", "varchar", "是", "否", "0123", "1", "D", "组织名");
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
        outIndexSQL(tableList,"2");
    }


}
