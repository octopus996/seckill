package com.zyd.seckill.service;



import com.zyd.seckill.entity.User;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyd.seckill.vo.LoginVo;
import com.zyd.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyd
 * @since 2021-10-09
 */
public interface UserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
}
