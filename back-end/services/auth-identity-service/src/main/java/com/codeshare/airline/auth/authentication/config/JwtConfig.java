package com.codeshare.airline.auth.authentication.config;

import com.codeshare.airline.auth.authentication.security.key.SigningKeyProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@AllArgsConstructor
public class JwtConfig {

    private final SigningKeyProvider signingKeyProvider;

    @Bean
    public JwtEncoder jwtEncoder() throws JOSEException {
        RSAKey rsaKey = (RSAKey) signingKeyProvider
                .getPrivateJwkSet()
                .getKeys()
                .get(0);

        JWKSource<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws JOSEException {
        RSAKey rsaKey = (RSAKey) signingKeyProvider
                .getPublicJwkSet()
                .getKeys()
                .get(0);

        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }
}
