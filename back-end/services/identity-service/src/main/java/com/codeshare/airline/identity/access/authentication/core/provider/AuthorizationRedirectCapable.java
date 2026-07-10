package com.codeshare.airline.identity.access.authentication.core.provider;

import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;

public interface AuthorizationRedirectCapable {

    String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String callbackUri,
            String state,
            String codeChallenge,
            String nonce
    );
}

