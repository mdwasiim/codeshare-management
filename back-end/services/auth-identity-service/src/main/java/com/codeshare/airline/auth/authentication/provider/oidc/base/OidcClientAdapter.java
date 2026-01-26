package com.codeshare.airline.auth.authentication.provider.oidc.base;

import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.OidcAuthenticatedUser;

public interface OidcClientAdapter {

    OidcAuthenticatedUser exchangeCodeForUser(String authorizationCode, IdentityProviderConfig identityProviderConfig);
}
