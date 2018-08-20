package com.hisuntech.service;

import com.hisuntech.entity.Table;
import com.hisuntech.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {

    public void transferExcel(String path){
        System.out.println(path);
        File sqlFile = null;
        try {
            sqlFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"static/sql/hold.txt");
            System.out.println(sqlFile.getAbsolutePath());
            System.out.println(sqlFile.getParentFile().getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Table> tableList = new ArrayList<>();
        tableList = TransferExcelUtil.readExcel(path);
        Map<String,List> map = GenerateSQLVersion1.outSql(tableList);
        //1.生成创建表的SQL
        List<StringBuffer> sqlList = map.get("createSqlList");
        //2.设置主键的SQL
        List<StringBuffer> sqlList2 = map.get("primarySqlList");
        //3.注释的SQL
        List<StringBuffer> sqlList3 = map.get("commentSQL");
        //4.生成索引
        List<StringBuffer> sqlList4 = CreateIndexUtil.outIndexSQL(tableList,"1");
        //5.输出到文件
        try {
            OutToFileUtil.outToFile(sqlList,sqlList2,sqlList3,sqlList4,tableList,sqlFile.getParentFile().getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
