package com.codeshare.airline.identity.authentication.provider;

import com.codeshare.airline.identity.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.authentication.domain.TenantContext;

public interface AuthorizationRedirectCapable {

    String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String state,
            String codeChallenge,
            String nonce
    );
}

