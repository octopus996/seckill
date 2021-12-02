package com.zyd.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.dao.TSeckillOrderMapper;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TSeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder> implements TSeckillOrderService {

    @Autowired
    private TSeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        TSeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if (null != seckillOrder){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty")){
            return -1L;
        }else{
            return 0L;
        }

    }


}
