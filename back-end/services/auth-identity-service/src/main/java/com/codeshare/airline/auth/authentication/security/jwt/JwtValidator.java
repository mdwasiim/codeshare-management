package com.codeshare.airline.auth.authentication.security.jwt;

import com.codeshare.airline.auth.authentication.exception.TokenValidationException;
import com.codeshare.airline.auth.authentication.security.key.SigningKeyProvider;
import com.codeshare.airline.core.enums.AuthSource;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;

@Service
public class JwtValidator {

    private static final String ISSUER = "https://auth.codeshare.io";
    private static final String AUDIENCE = "codeshare-api"; // optional but recommended

    private final PublicKey publicKey;

    public JwtValidator(SigningKeyProvider signingKeyProvider) throws JOSEException {
        this.publicKey = signingKeyProvider.getKeyPair().getPublic();
    }

    public JwtAuthenticationClaims validate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .requireAudience(AUDIENCE)
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            if (!"access".equals(tokenType) && !"refresh".equals(tokenType)) {
                throw new TokenValidationException("Invalid token type");
            }

            String subject = claims.getSubject();
            String tenant = claims.get("tenant", String.class);
            String authSource = claims.get("auth_source", String.class);

            List<String> rolesList = claims.get("roles", List.class);
            Set<String> roles = rolesList != null ? Set.copyOf(rolesList) : Set.of();

            return JwtAuthenticationClaims.builder()
                    .subject(subject)
                    .tenantCode(tenant)
                    .roles(roles)
                    .authSource(AuthSource.valueOf(authSource))
                    .build();

        } catch (JwtException e) {
            throw new TokenValidationException("Invalid or expired token "+ e);
        }
    }
}
