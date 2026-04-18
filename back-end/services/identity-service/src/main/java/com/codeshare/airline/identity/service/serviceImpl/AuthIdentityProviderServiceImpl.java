package com.codeshare.airline.identity.service.serviceImpl;


import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.identity.repository.OidcIdentityProviderRepository;
import com.codeshare.airline.identity.service.AuthIdentityProviderService;

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
