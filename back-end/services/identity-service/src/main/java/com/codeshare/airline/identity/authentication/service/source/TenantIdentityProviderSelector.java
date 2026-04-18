package com.codeshare.airline.identity.authentication.service.source;

import com.codeshare.airline.identity.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.authentication.domain.TenantContext;
import com.codeshare.airline.identity.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.core.enums.auth.AuthSource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TenantIdentityProviderSelector  {

    public IdentityProviderConfig select(TenantContext tenant,AuthSource authSource) {

        List<IdentityProviderConfig> providers = tenant.getIdentityProviders();

        if (providers == null || providers.isEmpty()) {
            throw new UnsupportedAuthenticationFlowException( "No identity providers configured for ingestion: " + tenant.getTenantCode());
        }
        // 1️⃣ If gateway explicitly requested a provider
        if ( authSource!= null) {
            return providers.stream()
                    .filter(IdentityProviderConfig::isEnabled)
                    .filter(p -> p.getAuthSource() == authSource)
                    .findFirst()
                    .orElseThrow(() -> new UnsupportedAuthenticationFlowException("Auth provider " + authSource +" not enabled for ingestion " + tenant.getTenantCode() ));
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

