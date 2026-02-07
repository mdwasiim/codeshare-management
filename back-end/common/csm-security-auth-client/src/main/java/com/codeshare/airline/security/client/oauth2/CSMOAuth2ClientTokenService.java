package com.codeshare.airline.security.client.oauth2;


import com.codeshare.airline.security.client.properties.CSMOAuth2ClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSMOAuth2ClientTokenService {

    private static final Authentication SYSTEM_AUTH =
            new UsernamePasswordAuthenticationToken(
                    "system",
                    "N/A",
                    List.of()
            );

    private final CSMOAuth2ClientProperties properties;
    private final OAuth2AuthorizedClientManager clientManager;

    /**
     * Default token (single gateway setup)
     */
    public String getAccessToken() {
        log.debug("getAccessToken() called using default registrationId");
        return getAccessToken(properties.getRegistrationId());
    }

    /**
     * Token per gateway registration
     */
    public String getAccessToken(String registrationId) {

        log.debug("Starting OAuth2 token acquisition [registrationId={}]", registrationId);

        OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest
                        .withClientRegistrationId(registrationId)
                        .principal(SYSTEM_AUTH)
                        .build();

        OAuth2AuthorizedClient client;

        try {
            client = clientManager.authorize(request);
        } catch (Exception ex) {
            log.error(
                    "OAuth2 token acquisition FAILED [registrationId={}]",
                    registrationId,
                    ex
            );
            throw ex;
        }

        if (client == null) {
            log.error(
                    "OAuth2AuthorizedClient is NULL [registrationId={}] – authorization failed",
                    registrationId
            );
            throw new IllegalStateException(
                    "Failed to acquire OAuth2 token for gateway: " + registrationId
            );
        }

        OAuth2AccessToken token = client.getAccessToken();

        if (token == null) {
            log.error(
                    "OAuth2AccessToken is NULL [registrationId={}] – authorization failed",
                    registrationId
            );
            throw new IllegalStateException(
                    "Failed to acquire OAuth2 token for gateway: " + registrationId
            );
        }

        log.debug(
                "OAuth2 token acquired successfully [registrationId={}, tokenType={}, expiresAt={}]",
                registrationId,
                token.getTokenType().getValue(),
                token.getExpiresAt()
        );

        if (token.getExpiresAt() != null) {
            long secondsLeft = token.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();

            log.trace(
                    "OAuth2 token validity [registrationId={}, expiresInSeconds={}]",
                    registrationId,
                    secondsLeft
            );
        }

        return token.getTokenValue();
    }
}

