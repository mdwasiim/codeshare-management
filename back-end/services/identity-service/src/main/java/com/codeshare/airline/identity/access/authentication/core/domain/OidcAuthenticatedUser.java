package com.codeshare.airline.identity.access.authentication.core.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class OidcAuthenticatedUser {

    private final Long userId;
    private final String subject;      // OIDC "sub"
    private final String username;
    private final String email;
    private final String issuer;
    private final String externalId;
}
