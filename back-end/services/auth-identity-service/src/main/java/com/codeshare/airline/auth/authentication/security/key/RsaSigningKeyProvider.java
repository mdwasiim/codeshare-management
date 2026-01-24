package com.codeshare.airline.auth.authentication.security.key;

import com.codeshare.airline.auth.authentication.config.SecurityProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
public class RsaSigningKeyProvider implements SigningKeyProvider {

    private final SecurityProperties securityProperties;

    private RSAKey rsaKey;

    @PostConstruct
    void init() {
        try {
            SecurityProperties.Jwt jwt = securityProperties.getJwt();
            SecurityProperties.Keystore ks = jwt.getKeystore();

            Resource keystoreResource = ks.getLocation();

            // ✅ PKCS12 for .p12 files
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            try (InputStream is = keystoreResource.getInputStream()) {
                keyStore.load(is, ks.getStorePassword().toCharArray());
            }

            Key key = keyStore.getKey(
                    ks.getKeyAlias(),
                    ks.getKeyPassword().toCharArray()
            );

            if (!(key instanceof RSAPrivateKey privateKey)) {
                throw new IllegalStateException("Key is not an RSA private key");
            }

            Certificate cert = keyStore.getCertificate(ks.getKeyAlias());
            RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

            // ✅ Stable KID (issuer-based)
            this.rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(jwt.getIssuer())
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
        // 🔐 INTERNAL USE ONLY
        return new JWKSet(rsaKey);
    }

    @Override
    public JWKSet getPublicJwkSet() {
        // 🔓 SAFE TO EXPOSE
        return new JWKSet(rsaKey.toPublicJWK());
    }
}
