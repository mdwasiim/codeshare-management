package com.codeshare.airline.auth.authentication.domain.model;

import com.codeshare.airline.core.enums.AuthSource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class IdentityProviderConfig {

    private AuthSource authSource;

    private boolean enabled;

    private int priority;

    private OidcConfig oidcConfig;

}
