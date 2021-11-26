package com.zyd.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*public void send(Object msg){
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend("fanoutExchang",null,msg);
    }*/

    public void send01(Object msg){
        log.info("queue_direct01发送消息"+msg);
        rabbitTemplate.convertAndSend("exchang_direct","routingkey01",msg);
    }
    public void send02(Object msg){
        log.info("queue_direct02发送消息"+msg);
        rabbitTemplate.convertAndSend("exchang_direct","routingkey02",msg);
    }
}
