package com.zyd.seckill.vo;

import com.zyd.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public class DetailVo {
    private User user;

    private GoodsVo goodsVo;

    private int seckillStatus;

    private int remainSeconds;

    public DetailVo() {
    }

    public DetailVo(User user, GoodsVo goodsVo, int seckillStatus, int remainSeconds) {
        this.user = user;
        this.goodsVo = goodsVo;
        this.seckillStatus = seckillStatus;
        this.remainSeconds = remainSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
