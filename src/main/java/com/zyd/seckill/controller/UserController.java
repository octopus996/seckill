package com.zyd.seckill.controller;

import com.zyd.seckill.entity.User;
import com.zyd.seckill.rabbitmq.MQSender;
import com.zyd.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    public RespBean info(User user){
        return RespBean.success();
    }
    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("hello");
    }
}
