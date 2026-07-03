package com.codeshare.airline.identity.access.authentication.core.service.core;

import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthenticationProvider;
import com.codeshare.airline.core.enums.auth.AuthSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProviderResolver providerResolver;
    private final TenantContextResolver tenantContextResolver;

    public AuthenticationResult authenticate(LoginRequest request) {

        AuthSource authSource = request.getAuthSource();

        if (authSource == null) {
            IdentityProviderConfig config = request.getIdentityProviderConfig();

            if (config == null || config.getAuthSource() == null) {
                throw new IllegalArgumentException("Unable to determine authentication source");
            }

            authSource = config.getAuthSource();
        }

        log.info("Authentication attempt | user={} source={}", request.getUsername(), authSource);

        AuthenticationProvider provider = providerResolver.resolve(authSource);

        if (provider == null) {
            throw new IllegalStateException("No provider found for auth source: " + authSource);
        }

        AuthenticationResult result = provider.authenticate(request);

        log.info("Authentication success | user={} source={}", result.getUsername(), authSource);

        return result;
    }

}
