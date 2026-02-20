package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.auth.repository.OidcIdentityProviderRepository;
import com.codeshare.airline.auth.service.AuthIdentityProviderService;
import com.codeshare.airline.core.enums.auth.AuthSource;

public class AuthIdentityProviderServiceImpl implements AuthIdentityProviderService {


    OidcIdentityProviderRepository oidcIdentityProviderRepository;

    @Override
    public void assertProviderEnabled(String tenantCode, AuthSource source) {

        oidcIdentityProviderRepository
                .findByTenant_TenantCodeAndAuthSource(tenantCode, source)
                .orElseThrow(() ->
                        new UnsupportedAuthenticationFlowException(
                                source + " authentication not enabled for ingestion"
                        )
                );
    }
}
