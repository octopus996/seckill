package com.zyd.seckill.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * UUID工具类
 */
@Component
public class UUIDUtil {
    /**
     * 生成32位字符串
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }


}
