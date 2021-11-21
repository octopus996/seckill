package com.zyd.seckill.vo;

import com.zyd.seckill.entity.TOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private TOrder order;

    private GoodsVo goodsVo;
}
