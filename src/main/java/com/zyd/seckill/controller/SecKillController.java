package com.zyd.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.tools.json.JSONUtil;
import com.zyd.seckill.entity.SeckillMessage;
import com.zyd.seckill.entity.TOrder;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.rabbitmq.MQSender;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.zyd.seckill.service.TSeckillOrderService;
import com.zyd.seckill.utils.JsonUtil;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    private Map<Long, Boolean> isStockEmpty = new HashMap<>();
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;

    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user, Long goodsId) {

        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        //根据商品ID查找该商品
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckllFail";
        }
        //判断是重复抢购
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );
        //判断不为空
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "seckillFail";
        }
        //抢购
        TOrder order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "OrderDetail";
    }

    @RequestMapping("/doSeckill")
    @ResponseBody
    public RespBean doSecKill(User user, Long goodsId) {

        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        /*//根据商品ID查找该商品
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是重复抢购
        *//*TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );*//*
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        //判断不为空
        if (seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //抢购
        TOrder order =orderService.seckill(user,goods);

        return RespBean.success(order);*/
        TSeckillOrder orderSeckill = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (null != orderSeckill) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记，减少访问reids
        if (isStockEmpty.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //从redis中取出该商品的库存，每运行一次减1
//        Long decrement = redisTemplate.opsForValue().decrement("stock" + goodsId);
        Long decrement = (Long) redisTemplate.execute(redisScript, Collections.singletonList("stock:" + goodsId),Collections.EMPTY_LIST);

        if (decrement < 0){
            isStockEmpty.put(goodsId,true);
           // redisTemplate.opsForValue().increment("stock:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage message = new SeckillMessage(user, goodsId);
        //object转Json
        mqSender.sendSeckill(JsonUtil.objectToJson(message));
        //返回0 表示排队中
        return RespBean.success(0);
    }

    /**
     * -1表示秒杀失败，0表示排队中，1表示秒杀成功
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/getResult")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if (null == user ){
            return null;
        }
        Long seckillOrderId= seckillOrderService.getResult(user,goodsId);
        return RespBean.success(seckillOrderId);
    }

    /**
     * 系统初始化，把商品库存放在redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            isStockEmpty.put(goodsVo.getId(),false);
            redisTemplate.opsForValue().set("stock:"+goodsVo.getId(),goodsVo.getStockCount());
        }
    }
}
