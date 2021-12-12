package com.zyd.seckill.vo;


import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@ToString
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    //用户名和密码校验
    LOGIN_ERROR(500210,"用户名或密码错误！"),
    MOBILE_ERROR(500211,"电话号码格式错误！"),
    BIND_ERROR(500212,"绑定异常！"),
    SESSION_ERROR(500213,"用户不存在！"),

    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品限购一个"),
    CHECK_CAPTCHA_ERROR(500502,"验证码错误，请重新输入！"),

    ORDER_NOT_EXIST(500300, "订单不存在");

    private final Integer code;//状态码
    private final String message;//消息



   /* RespBeanEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespBeanEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }*/
}
