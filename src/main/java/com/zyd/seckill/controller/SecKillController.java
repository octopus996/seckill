package com.zyd.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyd.seckill.entity.TOrder;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.zyd.seckill.service.TSeckillOrderService;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TOrderService orderService;


    @RequestMapping("/doSecKill")
    public String doSecKill(Model model, User user, Long goodsId){

        if (null == user){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckllFail";
        }
        //判断是重复抢购
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );
        //判断不为空
        if (seckillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "seckillFail";
        }
        //抢购
        TOrder order =orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "OrderDetail";
    }
}
