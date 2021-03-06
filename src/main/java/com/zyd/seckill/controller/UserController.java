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


    /*@RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("hello");
    }*/

    /**
     * Fanou模式
     */
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq02(){
        mqSender.send("hello");
    }

    /**
     * Direct模式
     *
     */
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void direct01(){
        mqSender.send01("hello queue_direct01");
    }

    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void direct02(){
        mqSender.send02("hello queue_direct02");
    }

    /**
     * Topic 模式
     */
    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void topic01(){
        mqSender.send03("hello queue_topic01");
    }

    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void topic02(){
        mqSender.send04("hello queue_topic02");
    }

    /**
     * headers模式
     */
    @RequestMapping("/mq/headers01")
    @ResponseBody
    public void headers01(){
        mqSender.send05("两个都接受消息");
    }
    @RequestMapping("/mq/headers02")
    @ResponseBody
    public void headers02(){
        mqSender.send06("queue01接受消息");
    }
}
