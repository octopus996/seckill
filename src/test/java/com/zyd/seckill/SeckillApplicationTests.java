package com.zyd.seckill;

import com.zyd.seckill.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Boolean> redisScript;

    @Test
    void contextLoads() {
    }

    @Test
    public void testLock(){
        String value = UUIDUtil.randomUUID().toString();
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent("k1", value, 120, TimeUnit.SECONDS);
        if (isLock){
            redisTemplate.opsForValue().set("name","nameValue");
            String name = (String) redisTemplate.opsForValue().get("name");
            System.out.println("name:"+name);
            System.out.println(redisTemplate.opsForValue().get("k1"));
            //Integer.parseInt("nameValue");
            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else{
            System.out.println("有线程在使用，请稍后！");
        }

    }
}
