package com.codeshare.airline.identity.authentication.config;

import com.codeshare.airline.identity.authentication.security.jwt.AudienceValidator;
import com.codeshare.airline.identity.authentication.security.key.SigningKeyProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@AllArgsConstructor
@Configuration
public class JwtConfig {

    private final SigningKeyProvider signingKeyProvider;
    private final SecurityProperties securityProperties;

    @Bean
    public JwtEncoder jwtEncoder() throws JOSEException {
        RSAKey rsaKey = (RSAKey) signingKeyProvider
                .getPrivateJwkSet()
                .getKeys()
                .getFirst();

        JWKSource<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws JOSEException {

        RSAKey rsaKey = (RSAKey) signingKeyProvider
                .getPublicJwkSet()
                .getKeys()
                .getFirst();

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
                        .build();

        String issuer = securityProperties.getJwt().getIssuer();
        String audience = securityProperties.getJwt().getAudience();

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(
                        JwtValidators.createDefaultWithIssuer(issuer),
                        new AudienceValidator(audience)
                );

        decoder.setJwtValidator(validator);

        return decoder;
    }
}
