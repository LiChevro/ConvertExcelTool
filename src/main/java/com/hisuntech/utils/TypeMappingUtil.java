package com.hisuntech.utils;

import com.hisuntech.entity.Table;

import java.util.List;

/**
 * @Desciption 读取Excel表里的数据类型转化为相应数据库的数据库类型
 * @create 2018.7
 */
public class TypeMappingUtil {

    public  static List<Table> ChangeTypeALL(List<Table> list){
        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i).getDatabaseBrand()){
                case "ORACLE" :
                    list= ChangeToOracle(list);
                    break;
                case "DB2" :
                    list= ChangeToDB2(list);
                    break;
                case "MYSQL":
                    list=ChangeToMySQL(list);
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    /**
     * 将表中的类型更变为对Oracle类型
     * @return
     */
    static List<Table> ChangeToOracle(List<Table> list){

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getFields().size(); j++) {
                switch (list.get(i).getFields().get(j).getFieldType()){
                    case "BIGINT" :
                        list.get(i).getFields().get(j).setFieldType("NUMBER");
                        break;
                    case "SMALLINT" :
                        list.get(i).getFields().get(j).setFieldType("NUMBER");
                        break;
                    case "DBCLOB" :
                        list.get(i).getFields().get(j).setFieldType("NCLOB");
                        break;
                    default:
                        break;
                }
            }
        }
        return list;

    }

    /**
     * 将表中的类型更变为对DB2类型
     * @return
     */
    static List<Table> ChangeToDB2(List<Table> list){


        return list;

    }

    /**
     * 将表中的类型更变为对MySQL类型
     * @return
     */
    static List<Table> ChangeToMySQL(List<Table> list){


        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getFields().size(); j++) {
                switch (list.get(i).getFields().get(j).getFieldType()){
                    case "CLOB" :
                        list.get(i).getFields().get(j).setFieldType("TEXT");
                        break;
                    case "DBCLOB" :
                        list.get(i).getFields().get(j).setFieldType("LONGTEXT");
                        break;
                    default:
                        break;
                }
            }

        }
        return list;

    }


}
