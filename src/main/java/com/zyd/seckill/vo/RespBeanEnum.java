package com.zyd.seckill.vo;


import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@ToString
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常");
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
