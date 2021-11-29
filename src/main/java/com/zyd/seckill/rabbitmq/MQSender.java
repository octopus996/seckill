package com.zyd.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Properties;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg){
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend("fanoutExchang","",msg);
    }

    public void send01(Object msg){
        log.info("queue_direct01发送消息"+msg);
        rabbitTemplate.convertAndSend("exchang_direct","queue.red",msg);
    }
    public void send02(Object msg){
        log.info("queue_direct02发送消息"+msg);
        rabbitTemplate.convertAndSend("exchang_direct","queue.green",msg);
    }

    public void send03(Object msg){
        log.info("queue_topic01发送消息:"+msg);
        rabbitTemplate.convertAndSend("exchang_topic","queue.topic",msg);
    }

    public void send04(Object msg){
        log.info("queue_topic02发送消息:"+msg);
        rabbitTemplate.convertAndSend("exchang_topic","queue.queue.topic",msg);
    }

    public void send05(String msg){
        log.info("两个queue都发送消息:"+msg);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","quickly");
        Message message=new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend("exchange_headers","",message);
    }

    public void send06(String msg){
        log.info("queue02发送消息:"+msg);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","low");
        Message message=new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend("exchange_headers","",message);

    }
}
