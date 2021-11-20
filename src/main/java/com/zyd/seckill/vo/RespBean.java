package com.zyd.seckill.vo;

import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


public class RespBean {
    private long code;//状态码
    private String message;//消息
    private Object obj;

    public RespBean() {
    }

    public RespBean(long code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "RespBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                '}';
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    /**
     * 成功的返回结果
     * @return
     */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }

    /**
     * 重载成功的返回结果
     * @param obj
     * @return
     */
    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    /**
     * 失败的返回结果
     * @param respBeanEnum
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    /**
     * 重载失败的返回结果
     * @param respBeanEnum
     * @param obj
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }
}
