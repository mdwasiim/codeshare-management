package com.codeshare.airline.auth.authentication.service.source;

import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.core.enums.auth.AuthSource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TenantIdentityProviderSelector  {

    public IdentityProviderConfig select(TenantContext tenant,AuthSource requestedAuthSource) {

        List<IdentityProviderConfig> providers = tenant.getIdentityProviders();

        if (providers == null || providers.isEmpty()) {
            throw new UnsupportedAuthenticationFlowException( "No identity providers configured for ingestion: " + tenant.getTenantCode());
        }
        // 1️⃣ If gateway explicitly requested a provider
        if (requestedAuthSource != null) {
            return providers.stream()
                    .filter(IdentityProviderConfig::isEnabled)
                    .filter(p -> p.getAuthSource() == requestedAuthSource)
                    .findFirst()
                    .orElseThrow(() -> new UnsupportedAuthenticationFlowException("Auth provider " + requestedAuthSource +" not enabled for ingestion " + tenant.getTenantCode() ));
        }

        // 2️⃣ Otherwise select highest-priority enabled provider
        return providers.stream()
                .filter(IdentityProviderConfig::isEnabled)
                .sorted(Comparator.comparingInt(
                        IdentityProviderConfig::getPriority
                ))
                .findFirst()
                .orElseThrow(() -> new UnsupportedAuthenticationFlowException("No enabled identity providers for ingestion: "+ tenant.getTenantCode()));
    }
}

