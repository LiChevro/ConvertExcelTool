package com.hisuntech.controller;



import com.hisuntech.service.TransferService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FileController {

    @Autowired
    private TransferService service;

    @RequestMapping("/ajax/upload")
    @ResponseBody
    public Map<String,Integer> fileUpload(@RequestParam("uploadExcel") MultipartFile file, HttpServletRequest request) throws FileNotFoundException {
            Map<String,Integer> map = new HashMap<>();
            if (file == null || file.isEmpty()){
                map.put("status",0);
                return map;
            }
            String fileName = file.getOriginalFilename();
            int size = (int)file.getSize();
            System.out.println(fileName + "-->" +size);
//            URL url = this.getClass().getClassLoader().getResource("static/upload");
//            String uploadFilePath = url.getPath();
//            File uploadFile = new File(uploadFilePath);
            String holdTxt = "hold.txt";
            File uploadFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"static/upload/"+holdTxt);
            if (!uploadFile.getParentFile().exists()){         //判断文件父目录是否存在
                uploadFile.getParentFile().mkdirs();
            }
            try{
                File saveFile = new File(uploadFile.getParentFile()+"/"+fileName);
                file.transferTo(saveFile);                          //保存文件
                service.transferExcel(saveFile.getAbsolutePath());       //调用转化工具
                map.put("status",1);
                return map;
            }catch (IllegalStateException  e){
                e.printStackTrace();
                map.put("status",0);
                return map;
            }catch (IOException e){
                e.printStackTrace();
                map.put("status",0);
                return map;
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

    //文件下载
    @RequestMapping("toDownload")
    public ResponseEntity<byte[]> download(HttpServletRequest request, @RequestParam("filename") String filename,
                                           Model model) throws IOException {
        //获取项目的根路径，但是IDEA跟eclipse不同，这里获取到的只是项目的根路径
//        String path = request.getServletContext().getRealPath("/");
        //下载的路径(获取SpringBoot Resource下static文件的路径)
        File excelFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"static/excel/"+filename);
/*        System.out.println(path);
        File file = new File(path + File.separator + filename);*/
        HttpHeaders httpHeaders = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
        httpHeaders.setContentDispositionFormData("attachment",downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(excelFile),
                httpHeaders, HttpStatus.CREATED);

    }





}
