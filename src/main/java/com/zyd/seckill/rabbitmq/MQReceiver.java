package com.zyd.seckill.rabbitmq;

import com.zyd.seckill.entity.SeckillMessage;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.zyd.seckill.utils.JsonUtil;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TOrderService orderService;

    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接受消息："+msg);
    }

    /**
     * fanout模式
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_fanout01")
    public void  receive_fanout01(Object msg){
        log.info("QUEUE01接受消息:"+msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive_fanout02(Object msg){
        log.info("QUEUE02接受消息:"+msg);
    }

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

    /**
     * Topic模式
     *
     * @param msg
     */
    @RabbitListener(queues = "queue_topic01")
    public void receive_Topic01(Object msg){
        log.info("queue_topic01接受消息:"+msg);
    }
    @RabbitListener(queues = "queue_topic02")
    public void receive_Topic02(Object msg){
        log.info("queue_topic02接受消息:"+msg);
    }

    /**
     * Headers模式
     *
     * @param message
     */
    @RabbitListener(queues = "queue_headers01")
    public void receive_headers01(Message message) {
        log.info("queue_headers01接受消息对象:"+message);
        log.info("queue_headers01接受消息:"+new String(message.getBody()));
    }
    @RabbitListener(queues = "queue_headers02")
    public void receive_headers02(Message message){
        log.info("queue_headers02接受消息对象:"+message);
        log.info("queue_headers02接收消息:"+new String(message.getBody()));
    }

    @RabbitListener(queues = "seckillQueue")
    public void receiveSeckill(String message){
        log.info("接受消息:"+message);
        SeckillMessage seckillMessage = JsonUtil.jsonToPojo(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1){
            return;
        }
        //判断是否重复抢购
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            return ;
        }
        //下单操作
        orderService.seckill(user,goodsVo);
    }

}
