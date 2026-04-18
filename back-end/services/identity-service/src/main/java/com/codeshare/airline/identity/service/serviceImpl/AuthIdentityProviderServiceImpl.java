package com.codeshare.airline.service.serviceImpl;

import com.codeshare.airline.identity.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airlinerepository.OidcIdentityProviderRepository;
import com.codeshare.airlineservice.AuthIdentityProviderService;
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
