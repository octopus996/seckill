package com.zyd.seckill.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
public class TSeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 秒杀价
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TSeckillGoods{" +
        "id=" + id +
        ", goodsId=" + goodsId +
        ", seckillPrice=" + seckillPrice +
        ", stockCount=" + stockCount +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        "}";
    }
}
