package com.hisuntech.utils;

import com.hisuntech.entity.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        FileWriter fw = null;
        PrintWriter pw = null;
        int t = 0;
        for (Table table : tables) {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            StringBuffer address = new StringBuffer();
            String fieldTableName = table.getTableEnName();
            address.append(savePath + "/"+fieldTableName).append(".sql");
            File f = new File(address.toString());
            fw = new FileWriter(f, false);
            pw = new PrintWriter(fw);
            if (list != null){
                pw.println(list.get(t));
            }
            if (list1 != null){
                pw.println(list1.get(t));
            }
            if (list2 != null){
                pw.println(list2.get(t));
            }
            if (list3 != null){
                pw.println(list3.get(t));
            }
            t++;
            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        }
    }

}