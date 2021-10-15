package com.zyd.seckill.controller;

import com.zyd.seckill.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @RequestMapping("/goodsList")
    public String goodsList(HttpSession session, Model model,@CookieValue("userTicket") String ticket){
        if (StringUtils.isEmpty(ticket)){
            return "login";
        }
        //获取cookie,强转为user
        User user = (User) session.getAttribute(ticket);
        if (null==user){
            return "login";
        }
        model.addAttribute("user",user);
        return "goodsList";
    }
}
