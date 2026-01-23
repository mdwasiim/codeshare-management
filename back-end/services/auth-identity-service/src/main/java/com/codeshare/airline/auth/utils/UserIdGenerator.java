package com.codeshare.airline.auth.utils;

import java.security.SecureRandom;

public final class UserIdGenerator {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private UserIdGenerator() {}

    public static String generate(String tenantCode) {
        return tenantCode  + random(8);
    }

    private static String random(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
