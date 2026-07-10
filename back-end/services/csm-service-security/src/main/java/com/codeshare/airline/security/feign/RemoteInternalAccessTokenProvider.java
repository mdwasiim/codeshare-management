package com.codeshare.airline.security.feign;

import com.codeshare.airline.security.config.IdentityServiceSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(InternalAccessTokenProvider.class)
public class RemoteInternalAccessTokenProvider implements InternalAccessTokenProvider {

    private final IdentityServiceTokenClient tokenClient;
    private final IdentityServiceSecurityProperties properties;

    private volatile String accessToken;
    private volatile Instant expiresAt = Instant.EPOCH;

    @Override
    public synchronized String getAccessToken() {
        Instant refreshAt = expiresAt.minusSeconds(30);
        if (accessToken == null || Instant.now().isAfter(refreshAt)) {
            IdentityServiceTokenClient.InternalServiceTokenResponse response =
                    tokenClient.issueServiceToken("Basic " + basicCredentials());

            accessToken = response.getAccessToken();
            expiresAt = Instant.now().plusSeconds(response.getExpiresIn());
        }
        return accessToken;
    }

    private String basicCredentials() {
        String raw = properties.getS2s().getClientId() + ":" + properties.getS2s().getClientSecret();
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
