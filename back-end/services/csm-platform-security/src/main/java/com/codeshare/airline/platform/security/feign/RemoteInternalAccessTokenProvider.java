package com.codeshare.airline.platform.security.feign;

import com.codeshare.airline.platform.security.config.IdentityServiceSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@RequiredArgsConstructor
public class RemoteInternalAccessTokenProvider implements InternalAccessTokenProvider {

    private final IdentityServiceTokenClient tokenClient;
    private final IdentityServiceSecurityProperties properties;

    private volatile String accessToken;
    private volatile Instant expiresAt = Instant.EPOCH;

    @Override
    public synchronized String getAccessToken() {
        Instant refreshAt = expiresAt.minusSeconds(30);
        if (accessToken == null || Instant.now().isAfter(refreshAt)) {
            IdentityServiceTokenClient.InternalServiceTokenEnvelope envelope =
                    tokenClient.issueServiceToken("Basic " + basicCredentials());
            IdentityServiceTokenClient.InternalServiceTokenResponse response =
                    envelope != null ? envelope.getData() : null;

            if (response == null || !StringUtils.hasText(response.getAccessToken()) || response.getExpiresIn() <= 0) {
                throw new IllegalStateException("Identity service did not return a usable internal access token");
            }

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
