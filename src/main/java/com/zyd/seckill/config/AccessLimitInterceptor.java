package com.zyd.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.UserService;
import com.zyd.seckill.utils.CookieUtil;
import com.zyd.seckill.vo.RespBean;
import com.zyd.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            //获取用户
            User user=getUser(request,response);
            UserContext.setHandler(user);
            HandlerMethod method = (HandlerMethod) handler;
            //获取方法中的注解
            AccessLimit methodAnnotation = method.getMethodAnnotation(AccessLimit.class);
            if (null == methodAnnotation){
                return false;
            }
            int maxCount = methodAnnotation.maxCount();
            int second = methodAnnotation.second();
            boolean needLogin = methodAnnotation.needLogin();
            StringBuffer key = request.getRequestURL();
            if (needLogin){
                if (null == user){
                    render(response,RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key.append(":").append(user.getId());
                Integer count = (Integer) redisTemplate.opsForValue().get(key);
                if (count == 0){
                    redisTemplate.opsForValue().set(key,1,second, TimeUnit.SECONDS);
                }else if (count<maxCount){
                    redisTemplate.opsForValue().increment(key);
                }else{
                    render(response,RespBeanEnum.ACCESS_LIMIT_REQUEST);
                }
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, RespBeanEnum respBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        RespBean error = RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }


    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        //获取cookie值
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)){
            return null;
        }
        //根据cookie获取user数据并返回，达到session共享
        return userService.getUserByCookie(ticket,request,response);
    }
}
