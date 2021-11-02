package com.zyd.seckill.controller;

import com.zyd.seckill.entity.TGoods;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.UserService;
import com.zyd.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
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
}
