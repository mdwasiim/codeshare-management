package com.codeshare.airline.auth.authentication.provider;

import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.TenantContext;

public interface AuthorizationRedirectCapable {

    String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String state,
            String codeChallenge,
            String nonce
    );
}

