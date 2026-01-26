package com.codeshare.airline.auth.authentication.security.jwt;

import com.codeshare.airline.auth.authentication.config.SecurityProperties;
import com.codeshare.airline.auth.authentication.exception.TokenValidationException;
import com.codeshare.airline.auth.authentication.security.key.SigningKeyProvider;
import com.codeshare.airline.core.enums.AuthSource;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtValidator {

    private final SigningKeyProvider signingKeyProvider;
    private final SecurityProperties securityProperties;

    public JwtAuthenticationClaims validate(String token) {
        try {
            PublicKey publicKey = signingKeyProvider.getKeyPair().getPublic();

            SecurityProperties.Jwt jwt = securityProperties.getJwt();

            var parser = Jwts.parserBuilder()
                    .requireIssuer(jwt.getIssuer())
                    .setSigningKey(publicKey);

            // Optional but recommended
            if (jwt.getAudience() != null) {
                parser.requireAudience(jwt.getAudience());
            }

            Claims claims = parser
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            if (!"access".equals(tokenType) && !"refresh".equals(tokenType)) {
                throw new TokenValidationException("Invalid token type");
            }

            String subject = claims.getSubject();
            String tenant = claims.get("tenant_code", String.class);
            String authSource = claims.get("auth_source", String.class);

            List<String> rolesList = claims.get("roles", List.class);
            Set<String> roles = rolesList != null ? Set.copyOf(rolesList) : Set.of();

            return JwtAuthenticationClaims.builder()
                    .subject(subject)
                    .tenantCode(tenant)
                    .roles(roles)
                    .authSource(AuthSource.valueOf(authSource))
                    .build();

        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenValidationException("Invalid or expired token"+ e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}

