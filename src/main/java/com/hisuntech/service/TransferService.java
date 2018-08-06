package com.hisuntech.service;

import com.hisuntech.entity.Table;
import com.hisuntech.utils.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    public void transferExcel(String path){
        List<Table> tableList = new ArrayList<>();
        tableList = TransferExcelUtil.readExcel(path);
        List<StringBuffer> sqlList = new ArrayList();
        List<StringBuffer> sqlList2 = new ArrayList();
        List<StringBuffer> sqlList3 = new ArrayList<>();
        List<StringBuffer> sqlTableSpace = new ArrayList<>();
        //1.转化为相应数据库的数据类型
        tableList = TypeMappingUtil.ChangeTypeALL(tableList);
        //2.转化SQL
        sqlList = GenerateSqlUtil.outCreatedSql(tableList);
        sqlList2 = GenerateSqlUtil.outCommentSql(tableList);
        //2.1 指定数据库表的表空间
        sqlTableSpace = GenerateSqlUtil.outTableSpace(tableList);
        //3.生成索引
        sqlList3 = CreateIndexUtil.outIndexSQL(tableList);
        //4.输出到文件
        try {
            outToFileUtil.outToFile(sqlList,sqlList2,sqlTableSpace,sqlList3,tableList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
