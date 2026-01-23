package com.codeshare.airline.auth.authentication.security.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HmacSigner {

    private static final String ALGORITHM = "HmacSHA256";

    private HmacSigner() {
        // utility class
    }

    public static String sign(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    ALGORITHM
            ));

            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(raw);

        } catch (Exception e) {
            throw new IllegalStateException("HMAC signing failed", e);
        }
    }

    public static boolean verify(String data, String signature, String secret) {
        String expected = sign(data, secret);
        return constantTimeEquals(expected, signature);
    }

    /**
     * Prevents timing attacks
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
