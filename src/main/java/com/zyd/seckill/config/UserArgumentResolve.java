package com.zyd.seckill.config;

import com.zyd.seckill.entity.User;
import com.zyd.seckill.service.UserService;
import com.zyd.seckill.utils.CookieUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserArgumentResolve implements HandlerMethodArgumentResolver {
    @Resource
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //获取请求参数
        Class<?> clazz = parameter.getParameterType();

        //如果为true进行到下一步
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getHandler();
    }
}
