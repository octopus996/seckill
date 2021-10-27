package com.zyd.seckill.service.impl;


import com.zyd.seckill.entity.User;
import com.zyd.seckill.dao.UserMapper;
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
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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



    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isBlank(mobile.trim()) || StringUtils.isBlank(password.trim())){
            //返回枚举类型
            return  RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }
        //根据手机号获取用户
        User user = userMapper.selectById(mobile.trim());
        if(null==user.getId()){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!MD5.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成Cookie
        String ticket= UUIDUtil.randomUUID();
        //设置session
        //request.getSession().setAttribute(ticket,user);
        //将用户信息存储redis
        redisTemplate.opsForValue().set("user:"+ticket,user);

        //设置cookie
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success();
    }

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
