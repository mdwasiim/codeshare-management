package com.codeshare.airline.identity.authentication.provider.oidc.base;

import com.codeshare.airline.identity.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.authentication.domain.OidcAuthenticatedUser;

public interface OidcClientAdapter {

    OidcAuthenticatedUser exchangeCodeForUser(String authorizationCode, IdentityProviderConfig identityProviderConfig);
}
