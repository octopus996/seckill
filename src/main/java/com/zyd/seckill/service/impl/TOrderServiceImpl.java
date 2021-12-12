package com.zyd.seckill.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zyd.seckill.entity.*;
import com.zyd.seckill.dao.TOrderMapper;
import com.zyd.seckill.exception.GlobalException;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyd.seckill.service.TSeckillGoodsService;
import com.zyd.seckill.service.TSeckillOrderService;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.OrderDetailVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private TSeckillGoodsService seckillGoodsService;
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TOrder seckill(User user, GoodsVo goods) {
        //获取秒杀商品信息
        TSeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goods.getId()));
        /*if (seckillGoods.getStockCount()>1){
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        }*/
        /*seckillGoodsService.updateById(seckillGoods);*/
        /*if(goods.getStockCount()>0){
            goods.setGoodsStock(goods.getGoodsStock()-1);
        }*/
        //goodsService.updateById(goods);
        //进行商品库存扣减
        TOrder isExistOrderByGoodsId = orderMapper.selectOne(new QueryWrapper<TOrder>().eq("user_id", user.getId()).eq("goods_id", goods.getId()));
        TSeckillOrder isExistSckillOrderByGoodsId = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goods.getId()));
        if (null != isExistOrderByGoodsId){
            return null;
        }
        if (null != isExistSckillOrderByGoodsId){
            return null;
        }
        boolean goodsUpdate = goodsService.update(new UpdateWrapper<TGoods>().setSql("goods_stock=goods_stock-1")
                .eq("id", goods.getId()).gt("goods_stock", 0));
        if (!goodsUpdate){
            return null;
        }else {
            //进行秒杀商品扣减
            boolean seckillGoodsUpdate = seckillGoodsService.update(new UpdateWrapper<TSeckillGoods>().setSql("stock_count=stock_count-1")
                    .eq("goods_id", goods.getId()).gt("stock_count", 0));

            if (!seckillGoodsUpdate) {
                redisTemplate.opsForValue().set("isStockEmpty" + goods.getId(), "0");
                return null;
            } else {

                //生成秒杀订单
                TOrder order = new TOrder();
                order.setOrderStatus(0);
                order.setUserId(user.getId());
                order.setGoodsId(goods.getId());
                order.setDeliveryAddrId(0L);
                order.setGoodsName(goods.getGoodsName());
                order.setGoodsCount(goods.getStockCount());
                order.setGoodsPrice(seckillGoods.getSeckillPrice());
                order.setOrderChannel(0);
                order.setCreateDate(new Date());
                orderMapper.insert(order);

                //生成秒杀订单
                TSeckillOrder seckillOrder = new TSeckillOrder();
                seckillOrder.setUserId(user.getId());
                seckillOrder.setOrderId(order.getId());
                seckillOrder.setGoodsId(goods.getId());
                seckillOrderService.save(seckillOrder);
                //将秒杀订单存入redis
                redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);

                return order;
            }
        }
    }

    @Override
    public OrderDetailVo getDetail(Long orderId) {
        if (null == orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        //获取订单信息
        TOrder order = orderMapper.selectById(orderId);
        //获取商品信息
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);


        return detailVo;
    }

    @Override
    public Boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (null == user || goodsId < 0 || StringUtils.isEmpty(captcha)){
            return false;
        }
        //获取正确的验证码答案
        String realCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);

        return realCaptcha.equals(captcha)?true:false;
    }
}
