package com.zyd.seckill.dao;

import com.zyd.seckill.entity.TGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyd.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<GoodsVo> findGoodsVo();
}
