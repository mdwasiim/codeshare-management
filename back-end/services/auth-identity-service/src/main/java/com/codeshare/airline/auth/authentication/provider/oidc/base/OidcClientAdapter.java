package com.codeshare.airline.auth.authentication.provider.oidc.base;

import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.OidcAuthenticatedUser;

public interface OidcClientAdapter {

    OidcAuthenticatedUser exchangeCodeForUser(String authorizationCode, IdentityProviderConfig identityProviderConfig);
}
