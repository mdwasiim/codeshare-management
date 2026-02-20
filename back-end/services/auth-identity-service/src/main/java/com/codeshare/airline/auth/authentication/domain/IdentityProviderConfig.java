package com.codeshare.airline.auth.authentication.domain;

import com.codeshare.airline.core.enums.auth.AuthSource;
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
