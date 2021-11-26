package com.zyd.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    /*@RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接受消息："+msg);
    }*/

    /**
     * fanout模式
     *
     * @param msg
     */
    /*@RabbitListener(queues = "queue_fanout01")
    public void  receive_fanout01(Object msg){
        log.info("QUEUE01接受消息:"+msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive_fanout02(Object msg){
        log.info("QUEUE02接受消息:"+msg);
    }*/

    /**
     * direct模式
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_direct01")
    public void receive_Direct01(Object msg){
        log.info("queue_direct01接受消息："+msg);
    }
    @RabbitListener(queues = "queue_direct02")
    public void receive_Direct02(Object msg){
        log.info("queue_direct02接受消息："+msg);
    }
}
