package com.codeshare.airline.schedule.source.security;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class DefaultCredentialResolver implements CredentialResolver {

    private static final String SECRET = "MySuperSecretKey"; // 16 bytes

    @Override
    public String decrypt(String encryptedValue) {

        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedValue);

            SecretKeySpec key =
                    new SecretKeySpec(SECRET.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt credential", e);
        }
    }
}