package com.zyd.seckill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zyd.seckill.entity.TGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends TGoods{
    private BigDecimal seckillPrice;
    private Integer stockCount;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    private Date endDate;

}
