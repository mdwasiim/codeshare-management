package com.codeshare.airline.identity.access.authentication.data;

import com.codeshare.airline.identity.access.authentication.entities.OidcConfigEntity;
import com.codeshare.airline.identity.access.authentication.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.identity.access.authentication.repository.OidcIdentityProviderRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.IdentityProviderSeed;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.OidcConfigSeed;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OidcIdentityProviderDataLoader {

    private final TenantRepository tenantRepository;
    private final OidcIdentityProviderRepository identityProviderRepository;
    private final IdentityBootstrapData bootstrapData;

    @Transactional
    public void load(UUID tenantId) {
        log.info("Bootstrapping identity providers for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        bootstrapData.identityProviders().forEach(seed -> bootstrapProvider(tenant, seed));

        log.info("Identity provider bootstrap completed");
    }

    private void bootstrapProvider(Tenant tenant, IdentityProviderSeed seed) {
        OidcIdentityProviderEntity provider = identityProviderRepository
                .findByTenant_IdAndAuthSource(tenant.getId(), seed.authSource())
                .orElseGet(() -> createProvider(tenant, seed));

        if (provider.isEnabled() != seed.enabled() || provider.getPriority() != seed.priority()) {
            provider.setEnabled(seed.enabled());
            provider.setPriority(seed.priority());
            identityProviderRepository.save(provider);
        }

        if (seed.oidc() != null) {
            ensureOidcConfig(provider, seed.oidc());
        } else {
            log.info("{} provider ready for tenant [{}]", seed.authSource(), tenant.getTenantCode());
        }
    }

    private OidcIdentityProviderEntity createProvider(Tenant tenant, IdentityProviderSeed seed) {
        OidcIdentityProviderEntity provider = OidcIdentityProviderEntity.builder()
                .tenant(tenant)
                .authSource(seed.authSource())
                .enabled(seed.enabled())
                .priority(seed.priority())
                .build();

        identityProviderRepository.save(provider);
        log.info("Created {} provider for tenant [{}]", seed.authSource(), tenant.getTenantCode());
        return provider;
    }

    private void ensureOidcConfig(OidcIdentityProviderEntity provider, OidcConfigSeed seed) {
        OidcConfigEntity existing = provider.getOidcConfig();
        if (existing != null && existing.getIssuerUri() != null) {
            return;
        }

        OidcConfigEntity config = OidcConfigEntity.builder()
                .identityProvider(provider)
                .issuerUri(seed.issuerUri())
                .authorizationUri(seed.authorizationUri())
                .tokenUri(seed.tokenUri())
                .jwkSetUri(seed.jwkSetUri())
                .clientId(seed.clientId())
                .clientSecretRef(seed.clientSecretRef())
                .redirectUri(seed.redirectUri())
                .grantType(seed.grantType())
                .clientAuthMethod(seed.clientAuthMethod())
                .scopes(seed.scopes())
                .enforceRedirectUri(seed.enforceRedirectUri())
                .build();

        provider.setOidcConfig(config);
        identityProviderRepository.save(provider);
        log.info("Ensured {} OIDC config for tenant [{}]",
                provider.getAuthSource(),
                provider.getTenant().getTenantCode());
    }

    public boolean isLoaded(UUID tenantId) {
        long count = identityProviderRepository.countByTenantId(tenantId);
        return count >= bootstrapData.identityProviders().size();
    }
}
