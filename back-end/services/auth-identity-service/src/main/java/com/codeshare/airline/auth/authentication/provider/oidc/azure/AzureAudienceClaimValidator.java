package com.codeshare.airline.auth.authentication.provider.oidc.azure;


import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class AzureAudienceClaimValidator implements OAuth2TokenValidator<Jwt> {

    private final String clientId;

    public AzureAudienceClaimValidator(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        List<String> audience = jwt.getAudience();

        if (audience.contains(clientId)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "Invalid audience", null)
        );
    }
}
