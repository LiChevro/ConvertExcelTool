package com.hisuntech.controller;


import com.hisuntech.service.TransferService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@Controller
public class FileUploadController {

    @Autowired
    private TransferService service;

    @RequestMapping("/ajax/upload")
    @ResponseBody
    public String fileUpload(@RequestParam(value = "file",required = false) MultipartFile file,HttpServletRequest request){

            System.out.println("formData:"+request.getParameter("form"));
            if (file == null || file.isEmpty()){
                return "false";
            }
            String fileName = file.getOriginalFilename();
            int size = (int)file.getSize();
            System.out.println(fileName + "-->" +size);
            String projectPath=request.getServletContext().getRealPath("");
            System.out.println("项目根路径"+projectPath);
            String path = "/upload";
            File dest = new File(projectPath+path + "/" +fileName);
            String realPath = projectPath+path+"/"+fileName;
            if (!dest.getParentFile().exists()){         //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            try{
                file.transferTo(dest);                          //保存文件
                service.transferExcel(realPath);       //调用转化工具
                return "true";
            }catch (IllegalStateException  e){
                e.printStackTrace();
                return "false";
            }catch (IOException e){
                e.printStackTrace();
                return "false";
            }

    }

    //多文件上传
    @RequestMapping("/ajax/uload")
    @ResponseBody
    public String uploads(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setCharacterEncoding("utf-8");
        List<MultipartFile> files = ((MultipartHttpServletRequest)req).getFiles("files");

        if(files.size() < 1){
            return "no choose";
        }

        for(MultipartFile file: files){
            byte[] bytes = file.getBytes();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                    new File(file.getOriginalFilename())));
            out.write(bytes);
            out.flush();
            out.close();
        }
        return "success";
    }

}
