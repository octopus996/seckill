package com.zyd.seckill.controller;

import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.UserService;
import com.zyd.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private UserService userService;
    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/goodsList" ,produces = "text/html;charset=utf-8")
    @ResponseBody
    //public String goodsList(HttpSession session, Model model, @CookieValue("userTicket") String ticket, HttpServletRequest request, HttpServletResponse response){
    public String goodsList(User user, Model model, HttpServletRequest request,
                            HttpServletResponse response){


        /*if (StringUtils.isEmpty(ticket)){
            return "login";
        }
        //获取cookie,强转为user
        //User user = (User) session.getAttribute(ticket);
        User user = userService.getUserByCookie(ticket,request,response);
        if (null==user){
            return "login";
        }*/

        //redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsList = goodsService.findGoodsVo();
        
        model.addAttribute("goodsList",goodsList);

        model.addAttribute("user",user);
        //如果为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }



        return html;
    }

    @RequestMapping("/toDetail/{goodsId}")
    @ResponseBody
    public String toDetail(@PathVariable Long goodsId, User user, Model model
        ,HttpServletResponse response,HttpServletRequest request){
        //从redis中获取页面
        String html ;
                html= (String) redisTemplate.opsForValue().get("goodsDetail" + goodsId);
        //如果redis中存在，直接返回页面
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //将redis获取的user传到前端
        model.addAttribute("user",user);
        //更具前台传来的商品id查询商品详情
        GoodsVo goods=goodsService.findGoodsVoByGoodsId(goodsId);




        //将商品详情传到前端
        model.addAttribute("goods", goods);

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
        /*return "goodsDetail";*/
        //手动渲染
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);

        //将页面缓存到redis中
        if (!StringUtils.isEmpty(html)){
            redisTemplate.opsForValue().set("goodsDetail",html);
        }
        return html;
    }
}
