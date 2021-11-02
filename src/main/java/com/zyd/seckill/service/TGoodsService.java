package com.zyd.seckill.service;

import com.zyd.seckill.entity.TGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyd.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
public interface TGoodsService extends IService<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
