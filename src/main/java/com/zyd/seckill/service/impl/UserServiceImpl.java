package com.zyd.seckill.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyd.seckill.dao.UserMapper;
import com.zyd.seckill.entity.User;

import com.zyd.seckill.exception.GlobalException;
import com.zyd.seckill.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyd.seckill.utils.CookieUtil;
import com.zyd.seckill.utils.MD5;
import com.zyd.seckill.utils.UUIDUtil;
import com.zyd.seckill.utils.ValidatorUtil;
import com.zyd.seckill.vo.LoginVo;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyd
 * @since 2021-10-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserService userService;


    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if(StringUtils.isBlank(mobile.trim()) || StringUtils.isBlank(password.trim())){
            //返回枚举类型
            throw new  GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            throw new  GlobalException(RespBeanEnum.MOBILE_ERROR);
        }
        //根据手机号获取用户
       User user = userService.getOne(new QueryWrapper<User>().eq("id", mobile));
        System.out.println(user);

        if (null == user.getRegisterDate()){
            user.setRegisterDate(new Date());
        }
        user.setLastLoginDate(new Date());
        user.setLoginCount(user.getLoginCount()+1);
        userMapper.updateById(user);

        if(null==user.getId()){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!MD5.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //生成Cookie
        String ticket= UUIDUtil.randomUUID();
        //设置session
        //request.getSession().setAttribute(ticket,user);



            //将用户信息存储redis
            redisTemplate.opsForValue().set("user:" + ticket, user);

            //设置cookie
            CookieUtil.setCookie(request, response, "userTicket", ticket);


            return RespBean.success();

    }
    //获取redis中的cookie
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if (null!=user){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
}
