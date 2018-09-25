package com.hisuntech.utils;


import com.hisuntech.entity.Field;
import com.hisuntech.entity.Table;
import com.hisuntech.service.MySQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Desciption 读取Excel表里的数据类型转化为相应数据库的数据库类型
 * @author ll
 * @create 2018.7
 */
public class TypeMappingUtil {

    private static final String _9 = "9";
    private static final String S9 = "S9";
    private static final String X = "X";
    private static final String D = "D";
    private static final String M = "M";
    private static final String C  = "C";
    private static final String B = "B";


    public static String transferMySQLFieldType(String fieldType){
        StringBuffer transferType = new StringBuffer();
        String regex = "[,?\\(|\\)+]";
        List<String> typeWordList = new ArrayList<>();
        String[] typeWordArr = fieldType.split(regex);
        typeWordList = Arrays.asList(typeWordArr);
        if (typeWordList.get(0).equals(_9) || typeWordList.contains(S9)){
            if (typeWordList.size() == 3){                          //S9(15,2)、9(15,2)
                transferType.append(MySQL.DECIMAL).append("(")
                                    .append(typeWordList.get(1)).append(",").append(typeWordList.get(2))
                                    .append(")");
            }else if (typeWordList.size() == 2){
                if (Integer.parseInt(typeWordList.get(1)) <= 9){            //S9(5)、9(15)
                    transferType.append(MySQL.INT);
                }else if (Integer.parseInt(typeWordList.get(1)) > 9 && Integer.parseInt(typeWordList.get(1)) <= 18){              //9(5)
                    transferType.append(MySQL.BIGINT);
                }
                transferType.append("(").append(typeWordList.get(1)).append(")");
            }
        }else if (typeWordList.contains(X)){                //X(13)
            transferType.append(MySQL.VARCHAR).append("(").append(typeWordList.get(1)).append(")");
        }else if (typeWordList.contains(M) || typeWordList.contains(C)){            //M(12),C(11)
            transferType.append(MySQL.VARCHAR).append("(").append(Integer.parseInt(typeWordList.get(1)) * 2).append(")");
        }else if (typeWordList.contains(B)){
            transferType.append(MySQL.VARBINARY).append("(").append(typeWordList.get(1)).append(")");
        }else if (typeWordList.contains(D)){
            if ("8".equals(typeWordList.get(1))){
                transferType.append(MySQL.DATE);                        //D(8)
            }else{
                transferType.append(MySQL.DATETIME);                     //D(14)、D(17)
            }
        }
        System.out.println("转化后的类型:"+transferType);
        return transferType.toString();
    }

    public static List<Table> excuteTrans(List<Table> tableList){

        tableList.forEach(table -> {
            String databaseBrand = table.getDatabaseBrand();
            if ("MySQL".equalsIgnoreCase(databaseBrand)){
                table.getFields().stream().map(field -> {
                    String fieldType = field.getFieldType().trim();
                    field.setFieldType(transferMySQLFieldType(fieldType));
                    return field;
                }).collect(Collectors.toList());
            }
        });
        return tableList;
    }


    public static void main(String[] args) {

        Field field = new Field("    GROUPNAME     ", "    组织名 ", "S9(14,2)", "否", "是", "", "001", "D", "组织名       ");
        Field field1 = new Field("   GROUPID      ", "   组织名 ", "9(14)  ", "是", "否", "", "001", "D", "组织名       ");
        Field field2 = new Field("   group123     ", "   组织123   ", "X(12) ", "是", "否", "123       ", "001", "D", "组织名     ");
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
        excuteTrans(tableList);
    }


}
