package com.codeshare.airline.identity.access.authentication.core.provider.oidc.base;

import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcAuthenticatedUser;

public interface OidcClientAdapter {

    OidcAuthenticatedUser exchangeCodeForUser(String authorizationCode, IdentityProviderConfig identityProviderConfig);
}
