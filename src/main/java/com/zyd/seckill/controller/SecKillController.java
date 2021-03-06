package com.zyd.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.tools.json.JSONUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.zyd.seckill.config.AccessLimit;
import com.zyd.seckill.entity.SeckillMessage;
import com.zyd.seckill.entity.TOrder;
import com.zyd.seckill.entity.TSeckillOrder;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.exception.GlobalException;
import com.zyd.seckill.rabbitmq.MQSender;
import com.zyd.seckill.service.TGoodsService;
import com.zyd.seckill.service.TOrderService;
import com.zyd.seckill.service.TSeckillOrderService;
import com.zyd.seckill.utils.JsonUtil;
import com.zyd.seckill.utils.MD5;
import com.zyd.seckill.utils.UUIDUtil;
import com.zyd.seckill.vo.GoodsVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    private Map<Long, Boolean> isStockEmpty = new HashMap<>();
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;

    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user, Long goodsId) {

        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        //????????????ID???????????????
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //????????????
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckllFail";
        }
        //?????????????????????
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );
        //???????????????
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "seckillFail";
        }
        //??????
        TOrder order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "OrderDetail";
    }

    @RequestMapping("/{path}/doSeckill")
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path,User user, Long goodsId) {

        if (null == user || goodsId<0 || StringUtils.isEmpty(path)) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        /*//????????????ID???????????????
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //????????????
        if (goods.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //?????????????????????
        *//*TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId)
        );*//*
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        //???????????????
        if (seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //??????
        TOrder order =orderService.seckill(user,goods);

        return RespBean.success(order);*/
        TSeckillOrder orderSeckill = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (null != orderSeckill) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //???????????????????????????reids
        if (isStockEmpty.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //???redis????????????????????????????????????????????????1
//        Long decrement = redisTemplate.opsForValue().decrement("stock" + goodsId);
        Long decrement = (Long) redisTemplate.execute(redisScript, Collections.singletonList("stock:" + goodsId),Collections.EMPTY_LIST);

        if (decrement < 0){
            isStockEmpty.put(goodsId,true);
           // redisTemplate.opsForValue().increment("stock:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage message = new SeckillMessage(user, goodsId);
        //object???Json
        mqSender.sendSeckill(JsonUtil.objectToJson(message));
        //??????0 ???????????????
        return RespBean.success(0);
    }

    /**
     * -1?????????????????????0??????????????????1??????????????????
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/getResult")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if (null == user ){
            return null;
        }
        Long seckillOrderId= seckillOrderService.getResult(user,goodsId);
        return RespBean.success(seckillOrderId);
    }


    @AccessLimit(second = 5,maxCount=5 ,needLogin=true)
    @RequestMapping("/path")
    @ResponseBody
    public RespBean path(Long goodsId, User user, String captcha, HttpServletRequest request  ){
        if (null == user || goodsId < 0 || StringUtils.isEmpty(captcha)){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //???????????????????????????
        Boolean check=orderService.checkCaptcha(user,goodsId,captcha);
        if (!check){
            return RespBean.error(RespBeanEnum.CHECK_CAPTCHA_ERROR);
        }
        String path= MD5.md5(UUIDUtil.randomUUID()+user.getId()+goodsId);

        return RespBean.success(path);
    }

    @RequestMapping("/captcha")
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if (null == user){
            throw new GlobalException(RespBeanEnum.SESSION_ERROR);
        }
        //????????????????????????????????????
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text());
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("????????????????????????",e.getMessage());
        }
    }

    /**
     * ???????????????????????????????????????redis???
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            isStockEmpty.put(goodsVo.getId(),false);
            redisTemplate.opsForValue().set("stock:"+goodsVo.getId(),goodsVo.getStockCount());
        }
    }
}
