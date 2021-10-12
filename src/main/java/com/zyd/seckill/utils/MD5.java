package com.zyd.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5 {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    private static final String salt="1a2b3c4d";

    /**
     * 从前端到后端
     *
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 从后端到数据库
     *
     * @param fromPass
     * @return
     */
    public static String formPassToDBPass(String fromPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+fromPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 从前端到数据库
     *
     * @param inputPass
     * @param salt
     * @return
     */
    public static String inputPassToDBPass(String inputPass,String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass=formPassToDBPass(formPass,salt);
        return dbPass;

    }

    public static void main(String[] args) {
        String input ="123456";
        String salt="1a2b3c4d";
        String inputPass = inputPassToFormPass(input);

        System.out.println(inputPass);
        System.out.println(formPassToDBPass(inputPass,salt));
        System.out.println(inputPassToDBPass(input,salt));
    }
}
