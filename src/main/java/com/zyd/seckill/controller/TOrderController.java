package com.zyd.seckill.controller;


import com.zyd.seckill.entity.User;
import com.zyd.seckill.exception.GlobalException;
import com.zyd.seckill.exception.GlobalExceptionHandler;
import com.zyd.seckill.service.TOrderService;
import com.zyd.seckill.vo.OrderDetailVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyd
 * @since 2021-11-01
 */
@Controller
@RequestMapping("/order")
public class TOrderController {

    @Resource
    private TOrderService orderService;

    @RequestMapping("/orderDetail")
    @ResponseBody
    public RespBean orderDetail(User user,Long orderId){
        if (null == user){
            throw new GlobalException(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detailVo=orderService.getDetail(orderId);
        return RespBean.success(detailVo);
    }
}

