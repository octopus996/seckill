package com.zyd.seckill.controller;

import com.zyd.seckill.entity.TGoods;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.UserService;
import com.zyd.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private UserService userService;
    @Autowired
    private TGoodsService goodsService;

    @RequestMapping("/goodsList")
    //public String goodsList(HttpSession session, Model model, @CookieValue("userTicket") String ticket, HttpServletRequest request, HttpServletResponse response){
    public String goodsList(User user, Model model){


        /*if (StringUtils.isEmpty(ticket)){
            return "login";
        }
        //获取cookie,强转为user
        //User user = (User) session.getAttribute(ticket);
        User user = userService.getUserByCookie(ticket,request,response);
        if (null==user){
            return "login";
        }*/
        List<GoodsVo> goodsList = goodsService.findGoodsVo();
        model.addAttribute("goodsList",goodsList);

        System.out.println(goodsService.findGoodsVo());

        model.addAttribute("user",user);
        return "goodsList";
    }
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(@PathVariable Long goodsId, User user, Model model){
        //将redis获取的user传到前端
        model.addAttribute("user",user);
        //更具前台传来的商品id查询商品详情
        GoodsVo goods=goodsService.findGoodsVoByGoodsId(goodsId);

        //将商品详情传到前端
        model.addAttribute("goods",goods);

        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate = new Date();
        //秒杀状态  1为开启 2为结束
        int seckillStatus= 0;
        //剩余开始时间
        int remainSeconds= 0;
        //秒杀还未开始
        if (nowDate.before(startDate)){
            remainSeconds= (int) ((startDate.getTime()-nowDate.getTime())/1000);
            //秒杀已经结束
        }else if (nowDate.after(endDate)){
            seckillStatus=2;
            remainSeconds=-1;
            //秒杀开始
        }else {
            seckillStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goodsDetail";
    }
}
