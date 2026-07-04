package com.codeshare.airline.identity.access.authentication.core.service.core;

import com.codeshare.airline.identity.access.authentication.core.config.SecurityProperties;
import com.codeshare.airline.identity.access.authentication.core.domain.TokenPair;
import com.codeshare.airline.identity.access.authentication.core.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.access.authentication.core.exception.RefreshTokenInvalidException;
import com.codeshare.airline.identity.access.authentication.core.exception.TokenValidationException;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.authentication.entities.AuthTokenExchangeEntity;
import com.codeshare.airline.identity.access.authentication.entities.RefreshToken;
import com.codeshare.airline.identity.access.authentication.repository.AuthTokenExchangeRepository;
import com.codeshare.airline.identity.access.authentication.repository.RefreshTokenRepository;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthTokenExchangeRepository authTokenExchangeRepository;

    private final AuthUserService authUserService;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;


    private final SecurityProperties securityProperties;


    /* =========================================================
       TOKEN ISSUANCE
       ========================================================= */

    @Transactional
    public TokenPair issueTokens(AuthenticationResult auth) {

        String accessToken = issueAccessToken(auth);
        String refreshToken = issueRefreshToken(auth);

        persistRefreshSession(refreshToken, auth);

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /* =========================================================
       REFRESH FLOW
       ========================================================= */

    @Transactional
    public TokenPair refreshTokens(String refreshToken) {

        RefreshToken session =
                refreshTokenRepository.findByTokenHash(hash(refreshToken))
                        .orElseThrow(() ->
                                new RefreshTokenInvalidException("Session not found")
                        );

        if (!session.isActive()) {
            throw new RefreshTokenInvalidException("Session revoked");
        }

        if (session.getExpiresAt().isBefore(Instant.now())) {
            throw new RefreshTokenInvalidException("Session expired");
        }

        Jwt jwt = decodeAndValidateRefreshToken(refreshToken);

        if (!jwt.getSubject().equals(session.getUsername())
                || !jwt.getClaimAsString("tenant_code").equals(session.getTenantCode())
                || !jwt.getId().equals(session.getJti())) {
            throw new RefreshTokenInvalidException("Token mismatch");
        }

        // 🔁 Rotate old session
        session.setActive(false);
        session.setLastUsedAt(Instant.now());
        refreshTokenRepository.save(session);

        AuthenticationResult auth = rebuildAuthResult(session);

        return issueTokens(auth);
    }

    @Transactional
    public void revokeSession(String refreshToken) {
        refreshTokenRepository.findByTokenHash(hash(refreshToken))
                .ifPresent(session -> {
                    session.setActive(false);
                    session.setLastUsedAt(Instant.now());
                    refreshTokenRepository.save(session);
                });
    }

    /* =========================================================
       ACCESS / REFRESH TOKEN CREATION
       ========================================================= */

    private String issueAccessToken(AuthenticationResult auth) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(securityProperties.getJwt().getIssuer())
                .audience(List.of(securityProperties.getJwt().getAudience()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(securityProperties.getJwt().getAccessTokenTtl()))
                .subject(auth.getUsername())
                .claim("tenant_id", auth.getTenantId())
                .claim("tenant_code", auth.getTenantCode())
                .claim("roles", auth.getRoles())
                .claim("auth_source", auth.getAuthSource().name())
                .claim("type", "access")
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private String issueRefreshToken(AuthenticationResult auth) {

        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(securityProperties.getJwt().getIssuer())
                .audience(List.of(securityProperties.getJwt().getAudience()))
                .id(jti)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(securityProperties.getJwt().getRefreshTokenTtl()))
                .subject(auth.getUsername())
                .claim("tenant_id", auth.getTenantId())
                .claim("tenant_code", auth.getTenantCode())
                .claim("auth_source", auth.getAuthSource().name()) // optional but good
                .claim("type", "refresh")
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }


    /* =========================================================
       REFRESH SESSION PERSISTENCE
       ========================================================= */

    private void persistRefreshSession(String refreshToken, AuthenticationResult auth) {

        Jwt jwt = jwtDecoder.decode(refreshToken);

        RefreshToken session =
                RefreshToken.builder()
                        .tokenHash(hash(refreshToken))
                        .jti(jwt.getId())
                        .tenantId(auth.getTenantId())
                        .tenantCode(auth.getTenantCode())
                        .username(auth.getUsername())
                        .authSource(auth.getAuthSource())
                        .active(true)
                        .createdAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(
                                securityProperties.getJwt().getRefreshTokenTtl()
                        ))
                        .build();

        refreshTokenRepository.save(session);
    }

    /* =========================================================
       PKCE VERIFICATION
       ========================================================= */

    public void verifyPkce(String verifier, String expectedChallenge) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(verifier.getBytes(StandardCharsets.US_ASCII));

            String actualChallenge = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);

            if (!actualChallenge.equals(expectedChallenge)) {
                throw new AuthenticationFailedException("PKCE verification failed");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationFailedException("PKCE error"+e);
        }
    }

    /* =========================================================
       TOKEN EXCHANGE (OIDC HANDOFF)
       ========================================================= */

    @Transactional
    public String createExchangeCode(TokenPair tokens, AuthenticationResult auth) {

        String exchangeCode = UUID.randomUUID().toString();

        authTokenExchangeRepository.save(
                AuthTokenExchangeEntity.builder()
                        .exchangeCode(exchangeCode)
                        .accessToken(tokens.getAccessToken())
                        .refreshToken(tokens.getRefreshToken())
                        .authSource(auth.getAuthSource())
                        .tenantCode(auth.getTenantCode())
                        .expiresAt(LocalDateTime.now().plusSeconds(60))
                        .used(false)
                        .build()
        );

        return exchangeCode;
    }

    @Transactional
    public TokenPair exchangeTokens(String exchangeCode) {

        AuthTokenExchangeEntity exchange =
                authTokenExchangeRepository.findByExchangeCode(exchangeCode)
                        .orElseThrow(() ->
                                new AuthenticationFailedException("Invalid exchange code")
                        );

        if (exchange.isUsed()
                || exchange.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationFailedException("Exchange code expired");
        }

        exchange.setUsed(true);
        authTokenExchangeRepository.save(exchange);

        return TokenPair.builder()
                .accessToken(exchange.getAccessToken())
                .refreshToken(exchange.getRefreshToken())
                .build();
    }

    /* =========================================================
       CLEANUP
       ========================================================= */

    @Scheduled(fixedDelay = 300_000)
    public void cleanupExpiredExchanges() {
        authTokenExchangeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private Jwt decodeAndValidateRefreshToken(String refreshToken) {
        try {
            Jwt jwt = jwtDecoder.decode(refreshToken);

            if (!"refresh".equals(jwt.getClaimAsString("type"))) {
                throw new RefreshTokenInvalidException("Not a refresh token");
            }

            if (jwt.getExpiresAt() == null
                    || jwt.getExpiresAt().isBefore(Instant.now())) {
                throw new RefreshTokenInvalidException("Refresh token expired");
            }

            return jwt;
        } catch (Exception ex) {
            throw new RefreshTokenInvalidException("Invalid refresh token");
        }
    }

    private AuthenticationResult rebuildAuthResult(RefreshToken refreshToken) {

        UserDetailsAdapter user =
                authUserService.getAuthUserByUsername(refreshToken.getUsername());

        if (user == null || !user.isEnabled()) {
            throw new RefreshTokenInvalidException("User disabled");
        }

        Set<String> roles =
                rolePermissionAssignmentService.resolveRoleCodes(user.getUserId());
        Set<String> permissions =
                rolePermissionAssignmentService.resolvePermissionCodes(user.getUserId());

        return AuthenticationResult.builder()
                .username(user.getUsername())
                .tenantId(user.getTenantId())
                .tenantCode(user.getTenantCode())
                .roles(roles)
                .permissions(permissions)
                .authSource(refreshToken.getAuthSource())
                .build();
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public long getAccessTokenTtl() {
        return securityProperties.getJwt().getAccessTokenTtl();
    }

    public Jwt validateRefreshToken(String token) {

        try {
            Jwt jwt = jwtDecoder.decode(token);

            // ✅ Validate type
            String type = jwt.getClaimAsString("type");
            if (!"refresh".equals(type)) {
                throw new TokenValidationException("Invalid token type");
            }

            // ✅ Check DB session (IMPORTANT)
            RefreshToken session = refreshTokenRepository
                    .findByTokenHash(hash(token))
                    .orElseThrow(() ->
                            new TokenValidationException("Session not found")
                    );

            if (!session.isActive()) {
                throw new TokenValidationException("Refresh token revoked");
            }

            if (session.getExpiresAt().isBefore(Instant.now())) {
                throw new TokenValidationException("Refresh token expired");
            }

            // ✅ Cross-check JWT vs DB (VERY IMPORTANT)
            if (!jwt.getId().equals(session.getJti())) {
                throw new TokenValidationException("Token mismatch");
            }

            return jwt;

        } catch (JwtException ex) {
            throw new TokenValidationException("Invalid or expired refresh token", ex);
        }
    }
}
