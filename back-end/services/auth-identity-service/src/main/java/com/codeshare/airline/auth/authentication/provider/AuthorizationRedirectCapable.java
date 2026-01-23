package com.codeshare.airline.auth.authentication.provider;

import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;

public interface AuthorizationRedirectCapable {

    public String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String state,
            String codeChallenge,
            String nonce
    );
}

