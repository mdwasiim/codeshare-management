package com.codeshare.airline.identity.integration.config;

import com.codeshare.airline.identity.access.authentication.core.config.SecurityProperties;
import com.codeshare.airline.identity.access.authentication.core.service.core.TokenService;
import com.codeshare.airline.platform.security.feign.InternalAccessTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class InternalServiceTokenProvider implements InternalAccessTokenProvider {

    private final TokenService tokenService;
    private final SecurityProperties securityProperties;

    private volatile String accessToken;
    private volatile Instant expiresAt = Instant.EPOCH;

    @Override
    public synchronized String getAccessToken() {
        Instant refreshAt = expiresAt.minusSeconds(30);
        if (accessToken == null || Instant.now().isAfter(refreshAt)) {
            accessToken = tokenService.issueServiceAccessToken(securityProperties.getS2s().getClientId());
            expiresAt = Instant.now().plusSeconds(tokenService.getAccessTokenTtl());
        }
        return accessToken;
    }
}
