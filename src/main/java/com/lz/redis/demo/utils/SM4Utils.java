package com.lz.redis.demo.utils;

import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class SM4Utils {

    //key必须是16字节，即128位
    final static String key = "sm4demo123456789";

    //指明加密算法和秘钥
    static SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());

    //加密为16进制，也可以加密成base64/字节数组
    public static String encryptSm4(String plaintext) {
        return sm4.encryptBase64(plaintext);
    }

    //解密
    public static String decryptSm4(String ciphertext) {
        return sm4.decryptStr(ciphertext);
    }

    public static void main(String[] args) {
        String change = "改善目标";
        String target = "高蛋白";
        String add = "便秘";
        String tarStr = encryptSm4(target);
        String addStr = encryptSm4(add);
        String changeStr = encryptSm4(change);

        System.out.println("高蛋白------"+tarStr);
        System.out.println("便秘------"+addStr);
        System.out.println("改善目标------"+changeStr);
    }

}
