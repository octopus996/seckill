package com.zyd.seckill.service;

import com.zyd.seckill.entity.TSeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyd.seckill.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
public interface TSeckillOrderService extends IService<TSeckillOrder> {

    Long getResult(User user, Long goodsId);
}
