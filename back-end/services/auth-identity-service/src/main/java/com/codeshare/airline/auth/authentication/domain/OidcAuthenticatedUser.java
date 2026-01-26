package com.codeshare.airline.auth.authentication.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OidcAuthenticatedUser {

    private final UUID userId;
    private final String subject;      // OIDC "sub"
    private final String username;
    private final String email;
    private final String issuer;
    private final String externalId;
}
