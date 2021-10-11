package com.zyd.seckill.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/index")
    public String toIndex(Model model, HttpSession session){
        model.addAttribute("name","zyd");
        session.setAttribute("title","欢迎光临！");
        return "index";
    }
}
