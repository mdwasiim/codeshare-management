package com.codeshare.airline.identity.access.authentication.core.domain;

import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IdentityProviderConfig {

    private AuthSource authSource;

    private boolean enabled;

    private int priority;

    private String providerId; // e.g. "azure-ad", "okta", "google"

    private OidcConfig oidcConfig;

}
