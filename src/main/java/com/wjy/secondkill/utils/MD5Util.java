package com.wjy.secondkill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/1/23 15:17
 */
@Component
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String  salt = "1a2b3c4d";

    /**
     * 第一次加密，在前端返回的密码
     */
    public static String inputPassToFromPass(String inputPass){
        String str = ""+ salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDbPass(String fromPass, String salt){
        String str = ""+ salt.charAt(0)+salt.charAt(2)+fromPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDbPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        //一次加密d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFromPass("123456"));
        //二次加密b7797cce01b4b131b433b6acf4add449
        System.out.println(fromPassToDbPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
    }
}
