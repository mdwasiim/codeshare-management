package com.codeshare.airline.auth.authentication.config;

import com.codeshare.airline.auth.authentication.security.key.SigningKeyProvider;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

    @Bean
    JwtEncoder jwtEncoder(SigningKeyProvider keyProvider) {

        JWKSource<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(keyProvider.getPrivateJwkSet());

        return new NimbusJwtEncoder(jwkSource);
    }
}
