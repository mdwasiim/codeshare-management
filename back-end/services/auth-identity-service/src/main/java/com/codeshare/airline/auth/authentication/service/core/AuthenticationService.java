package com.codeshare.airline.auth.authentication.service.core;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.core.enums.AuthSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProviderResolver providerResolver;
    private final TenantContextResolver tenantContextResolver;

    public AuthenticationResult authenticate(LoginRequest request) {

        //IdentityProviderConfig identityProviderConfig = tenantContextResolver.resolveAuthType(ingestion);
        AuthSource authSource = request.getRequestedAuthSource();
        if(authSource==null){
            IdentityProviderConfig identityProviderConfig = request.getIdentityProviderConfig();
            authSource = identityProviderConfig.getAuthSource();
        }


        AuthenticationProvider provider = providerResolver.resolve(authSource);

        return provider.authenticate(request);
    }

}
