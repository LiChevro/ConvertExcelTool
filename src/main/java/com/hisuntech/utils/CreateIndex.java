package com.hisuntech.utils;

import com.hisuntech.entity.Field;
import com.hisuntech.entity.Index;
import com.hisuntech.entity.Table;
import com.hisuntech.service.SqlWords;

import java.util.*;

/**
 * @Description 创建索引
 * @author ll
 * @created 2018.9.4
 */
public class CreateIndex {

    private static String U = "U";
    private static String D = "D";
    private static String Y  = "是";
    private static String N = "否";

    /**
     * @Description 获取索引集合
     * @param table
     * @return
     */
    public static List<Index> getIndexList(Table table,String version){
        List<Field> fieldList = table.getFields();
        List<Index> indexList = new ArrayList<>();
        Iterator<Field> iterator = fieldList.iterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            if ("2".equals(version)){
                if (Y.equals(field.getIsPrimaryKey())){             //版本二的索引，将主键设为唯一索引
                    String tableEnName = table.getTableEnName();
                    String fieldEnName = field.getFieldEnName();
                    String indexNum = "0";                               //主键的索引编号写死为0
                    String indexType = "U";                                //设为唯一索引
                    List<String> indexNumList = Arrays.asList(indexNum.split(","));
                    List<String> indexTypeList = Arrays.asList(indexType.split(","));
                    Index index = new Index(tableEnName,fieldEnName,indexNumList,indexTypeList);
                    indexList.add(index);
                }
            }
            if (field.getIndexNum() == null || field.getIndexType() == null
                    || field.getIndexNum().equals("") || field.getIndexType().equals("")){
                if (field.getIsPrimaryKey().equals(Y) && version.equals("2")){
                    continue;
                }
                iterator.remove();                                            //移除没有索引的项，索引编号与索引类型缺一不可
                continue;
            }
            String tableEnName = table.getTableEnName();
            String fieldEnName = field.getFieldEnName();
            String indexNum = field.getIndexNum();
            String indexType = field.getIndexType();
            List<String> indexNumList = Arrays.asList(indexNum.split(","));
            List<String> indexTypeList = Arrays.asList(indexType.split(","));
            Index index = new Index(tableEnName,fieldEnName,indexNumList,indexTypeList);
            indexList.add(index);
        }
        //对indexList进行排序
        Collections.sort(indexList,(index1,index2)->{
            List<String> indexNum1 = index1.getIndexNum();
            List<String> indexNum2 = index2.getIndexNum();
            if (indexNum1.size() == indexNum2.size()){
                for (int i=0; i<indexNum1.size(); i++){
                    if (Integer.parseInt(indexNum1.get(i)) == Integer.parseInt(indexNum2.get(i))){                 //如果索引编号个数相同，则比较每一位的大小升序排序
                        continue;
                    }else if (Integer.parseInt(indexNum1.get(i)) > Integer.parseInt(indexNum2.get(i))){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            }else if (indexNum1.size() < indexNum2.size()){
                for (int i=0; i<indexNum1.size(); i++){
                    if (Integer.parseInt(indexNum1.get(i)) == Integer.parseInt(indexNum2.get(i))){                 //首先按照索引编号的每一个数字大小升序排序
                        continue;
                    }else if (Integer.parseInt(indexNum1.get(i))> Integer.parseInt(indexNum2.get(i))){
                        return 1;
                    }else{
                        return -1;
                    }
                }
                return -1;                                                                                   //如果索引编号的每一位数字都相同，则按长度升序排序
            }else{
                for (int i=0; i<indexNum2.size(); i++){
                    if (Integer.parseInt(indexNum1.get(i)) == Integer.parseInt(indexNum2.get(i))){                 //首先按照索引编号的每一个数字大小升序排序
                        continue;
                    }else if (Integer.parseInt(indexNum1.get(i))> Integer.parseInt(indexNum2.get(i))){
                        return 1;
                    }else{
                        return -1;
                    }
                }
                return -1;                                                                               //如果索引编号的每一位数字都相同，则按长度升序排序
            }
            return 0;
        });
        System.out.println("排序后的结果："+indexList);
        return indexList;
    }


    /**
     * @Description 拼接index
     * @param indexList
     * @return
     */
    public static StringBuffer appendIndexSql(List<Index> indexList){
        int count1= 0;
        StringBuffer indexSql = new StringBuffer();
        String[][] situation = new String[indexList.size()][5];            //引入situation二维数据记录已使用的索引编号的位置以及值
        for (Index index:indexList){                                                    //遍历每一个index对象

            boolean flag1 = false;
            boolean flag2 = false;

            List<String> indexNumList = index.getIndexNum();
            List<String> indexTypeList = index.getIndexType();

            for (int i=0; i<indexNumList.size(); i++){                          //遍历每一个indexNum

                String tableEnName = index.getTableEnName().trim();
                String fieldEnName = index.getFieldEnName().trim();

                String indexNum = indexNumList.get(i);
                //每次先与situation中的索引比较，观察是否是已使用的重复的索引编号
                for (int m=0; m<indexList.size(); m++){
                    for (int n=0; n<5; n++){
                        if (indexNum.equals(situation[m][n])){
                            flag1 = true;
                            break;
                        }
                    }
                    if (flag1){
                        flag2 = true;
                        break;
                    }
                }
                if (flag2){
                    flag1 = false;
                    flag2 = false;                      //将flag1，flag2标志位还原
                    continue;
                }
                indexSql.append(SqlWords.CREATE).append("  ");
                if (indexTypeList.get(i).equals(D)){
                    indexSql.append(SqlWords.INDEX).append("  ").append(tableEnName.toUpperCase()).append("_IDX").append(indexNum)
                            .append("  ").append(SqlWords.ON).append("  ").append(tableEnName).append("(").append(fieldEnName);
                }
                if (indexTypeList.get(i).equals(U)){
                    indexSql.append(SqlWords.UNIQUE_INDEX).append("  ").append(tableEnName.toUpperCase()).append("_IDX").append(indexNum)
                            .append("  ").append(SqlWords.ON).append("  ").append(tableEnName).append("(").append(fieldEnName);
                }
                for (int j=count1+1; j<indexList.size(); j++){                    //遍历之后的index对象中是否有相同的indexNum
                    Index index1 = indexList.get(j);
                    List<String> indexNumList1 = index1.getIndexNum();
                    if (indexNumList1.contains(indexNum)){
                        situation[j][indexNumList1.indexOf(indexNum)] = indexNum;         //将已使用的该索引的值，以及位置记录下来
                        String fieldEnNameDep = index1.getFieldEnName().trim();
                        indexSql.append(",").append(fieldEnNameDep);
                    }
                }
                indexSql.append(");\n");
            }
            count1++;
        }
        System.out.println("索引：\n"+indexSql);
        return indexSql;
    }

    /**
     * @Description 输出创建索引的SQL
     * @param tableList
     * @param version
     * @return
     */
    public static List<StringBuffer> outIndexSql(List<Table> tableList,String version){
        List<Index> indexList = new ArrayList<>();
        List<StringBuffer> indexSqlList = new ArrayList<>();
        for (Table table:tableList){
            indexList = getIndexList(table,version);
            StringBuffer indexSql = appendIndexSql(indexList);
            indexSqlList.add(indexSql);
        }
        return indexSqlList;
    }

    public static void main(String[] args) {
        Field field = new Field("groupName  ", "组织名", "varchar", "是", "是", "", "", "", "组织名");
        Field field1 = new Field("groupid  ", "组织名", "int", "是", "否", "", "1,2", "D,U", "组织名");
        Field field2 = new Field("  group123", "组织123", "varchar", "是", "否", "0123", "1,3", "D,U", "组织名");
        Field field3 = new Field("   groupSituation", "组织地址", "varchar", "是", "否", "25", "1", "D", "组织名");
        List<Field> list = new ArrayList<>();
        list.add(field);
        list.add(field1);
        list.add(field2);
        list.add(field3);
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
        outIndexSql(tableList,"2");
    }
}
