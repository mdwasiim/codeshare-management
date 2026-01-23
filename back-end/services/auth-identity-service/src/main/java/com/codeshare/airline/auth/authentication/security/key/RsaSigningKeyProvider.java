package com.codeshare.airline.auth.authentication.security.key;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RsaSigningKeyProvider implements SigningKeyProvider {

    @Value("${security.jwt.keystore.location}")
    private Resource keystore;

    @Value("${security.jwt.keystore.store-password}")
    private String storePassword;

    @Value("${security.jwt.keystore.key-alias}")
    private String keyAlias;

    @Value("${security.jwt.keystore.key-password}")
    private String keyPassword;

    private RSAKey rsaKey;

    @PostConstruct
    void init() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");

            try (InputStream is = keystore.getInputStream()) {
                keyStore.load(is, storePassword.toCharArray());
            }

            Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
            if (!(key instanceof RSAPrivateKey)) {
                throw new IllegalStateException("Key is not an RSA private key");
            }

            Certificate cert = keyStore.getCertificate(keyAlias);
            RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

            this.rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey((RSAPrivateKey) key)
                    .keyID(UUID.randomUUID().toString())
                    .build();

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load RSA signing key", ex);
        }
    }

    @Override
    public KeyPair getKeyPair() throws JOSEException {
        return new KeyPair(
                rsaKey.toRSAPublicKey(),
                rsaKey.toRSAPrivateKey()
        );
    }

    @Override
    public JWKSet getPrivateJwkSet() {
        return new JWKSet(rsaKey); // 🔐 contains private key
    }

    @Override
    public JWKSet getPublicJwkSet() {
        return new JWKSet(rsaKey.toPublicJWK()); // 🔓 safe to expose
    }
}
