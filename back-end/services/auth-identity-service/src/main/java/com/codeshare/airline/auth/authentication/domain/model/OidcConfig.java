package com.codeshare.airline.auth.authentication.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OidcConfig {

    private String issuerUri;

    private String authorizationUri;

    private String tokenUri;

    private String jwkSetUri;

    private String clientId;

    private String clientSecretRef;

    private String redirectUri;

    private String scopes;

    private boolean enforceRedirectUri;

}
