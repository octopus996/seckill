package com.zyd.seckill.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyd.seckill.entity.TOrder;
import com.zyd.seckill.dao.TOrderMapper;
import com.zyd.seckill.entity.TSeckillGoods;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.exception.GlobalException;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyd.seckill.service.TSeckillGoodsService;
import com.zyd.seckill.service.TSeckillOrderService;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.OrderDetailVo;
import com.zyd.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public TOrder seckill(User user, GoodsVo goods) {
        //获取秒杀商品信息
        TSeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goods.getId()));
        if (seckillGoods.getStockCount()>1){
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        }
        seckillGoodsService.updateById(seckillGoods);
        if(goods.getStockCount()>0){
            goods.setGoodsStock(goods.getGoodsStock()-1);
        }
        goodsService.updateById(goods);
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


        return order;
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
}
