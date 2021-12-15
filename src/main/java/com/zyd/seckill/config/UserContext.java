package com.zyd.seckill.config;

import com.zyd.seckill.entity.User;

public class UserContext {
    private static ThreadLocal<User> handler=new ThreadLocal<>();

    public static void setHandler(User user) {
        handler.set(user);
    }

    public static User getHandler(){
        return handler.get();
    }
}
