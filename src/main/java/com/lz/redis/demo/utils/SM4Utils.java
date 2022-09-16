package com.lz.redis.demo.utils;

import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class SM4Utils {

    //key必须是16字节，即128位
    final static String key = "sm4demo123456789";

    //指明加密算法和秘钥
    static SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());

    //加密为16进制，也可以加密成base64/字节数组
    public static String encryptSm4(String plaintext) {
        String encStr = sm4.encryptBase64(plaintext);
        encStr = "ENC("+encStr+")";
        return encStr;
    }

    //解密
    public static String decryptSm4(String ciphering) {
        ciphering = ciphering.substring(4,ciphering.length()-1);
        return sm4.decryptStr(ciphering);
    }

    public static void main(String[] args) {
        String change = "指以自行车做为工具比赛的体育运动。1896年第一届奥林匹克运动会上被列为正式比赛项目。平时骑自行车可以燃脂，增强腿部力量和提高心肺功能。";

        String changeStr = encryptSm4(change);


        System.out.println("改善目标------"+changeStr);
    }

}
