package com.hisuntech.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据库信息
 */
public class DatabaseInfo {

    private String DatabaseName;        //数据库名
    private String SchemaName;          //模式名
    private String tableEnName;          //英文表名
    private String tableChName;         //中文表名
    private Date create;                            //创建日期
    private String tableSpace;              //表空间
    private String indexSpace;              //索引空间
    private Long productDate;               //目前生产数量
    private Integer disk;                           //占用磁盘

}
