package com.zyd.seckill.service;

import com.zyd.seckill.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
public interface TOrderService extends IService<TOrder> {

    TOrder seckill(User user, GoodsVo goods);

    OrderDetailVo getDetail(Long orderId);

}
