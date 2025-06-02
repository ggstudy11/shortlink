package com.example.shortlink.admin.util;

import java.security.SecureRandom;

/**
 * 随机字符生成器
 */
public final class RandomGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成指定长度随机字母数字字符串
     * @return 随机字符串
     */
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    public static String generate() {
        return generate(6);
    }
}
