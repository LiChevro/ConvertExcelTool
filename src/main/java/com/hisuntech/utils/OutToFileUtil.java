package com.hisuntech.utils;

import com.hisuntech.entity.Table;

import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * 〈将sql语句输出到各个txt〉<br>
 * 〈leiui〉
 *
 * @author Shmily
 * @create 2018/8/2
 * @since 1.0.0
 */
public class OutToFileUtil {

    public static void outToFile(List<StringBuffer> list, List<StringBuffer> list1,List<StringBuffer> list2,
                                 List<StringBuffer> list3,List<Table> tables,String savePath) throws IOException {
        FileOutputStream fos = null;        //改用FileOutputStream解决utf-8乱码的问题
        PrintStream ps = null;                   //改用PrintStream解决utf-8乱码的问题
        int t = 0;
        for (Table table : tables) {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            StringBuffer address = new StringBuffer();
            String fieldTableName = table.getTableEnName();
            address.append(savePath + "/"+fieldTableName).append(".sql");
            File f = new File(address.toString());
            fos = new FileOutputStream(f,false);
            ps = new PrintStream(fos);
            if (list != null && list.size() != 0){
                ps.println(list.get(t));
            }
            if (list1 != null && list1.size() != 0){
                ps.println(list1.get(t));
            }
            if (list2 != null && list2.size() != 0){
                ps.println(list2.get(t));
            }
            if (list3 != null && list3.size() != 0){
                ps.println(list3.get(t));
            }
            t++;
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
        }
    }

}