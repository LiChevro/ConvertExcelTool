package com.hisuntech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String toIndex(){
        return "/index";
    }

    @RequestMapping("/transfer")
    public String toTransfer(){
        return "/upload";
    }

    @RequestMapping("/download")
    public String download(){
        return "download";
    }


}
