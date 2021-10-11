package com.zyd.seckill.controller;

import com.zyd.seckill.service.UserService;
import com.zyd.seckill.vo.LoginVo;
import com.zyd.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/login")

public class LoginController {

    @Resource
    private UserService userService;
    /**
     * 去到登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    public RespBean doLogin(LoginVo loginVo){
        return userService.doLogin(loginVo);
    }

}
