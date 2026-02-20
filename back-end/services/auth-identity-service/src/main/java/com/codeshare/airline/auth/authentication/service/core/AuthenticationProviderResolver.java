package com.codeshare.airline.auth.authentication.service.core;

import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.core.enums.auth.AuthSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class AuthenticationProviderResolver {

    private final Map<AuthSource, AuthenticationProvider> providerMap;

    public AuthenticationProviderResolver(
            List<AuthenticationProvider> providers
    ) {
        this.providerMap = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        AuthenticationProvider::getAuthSource,
                        Function.identity(),
                        (p1, p2) -> {
                            throw new IllegalStateException(
                                    "Duplicate AuthenticationProvider for auth source "
                                            + p1.getAuthSource()
                                            + ": " + p1.getClass().getName()
                                            + " vs " + p2.getClass().getName()
                            );
                        }
                ));
    }

    public AuthenticationProvider resolve(AuthSource authSource) {

        if (authSource == null) {
            throw new UnsupportedAuthenticationFlowException(
                    "AuthSource must be provided"
            );
        }

        AuthenticationProvider provider = providerMap.get(authSource);

        if (provider == null) {
            throw new UnsupportedAuthenticationFlowException(
                    "No authentication provider registered for: " + authSource
            );
        }

        return provider;
    }

    // Optional but useful
    public Set<AuthSource> supportedAuthSources() {
        return providerMap.keySet();
    }
}



