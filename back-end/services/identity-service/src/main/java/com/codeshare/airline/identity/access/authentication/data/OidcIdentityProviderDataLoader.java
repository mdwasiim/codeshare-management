package com.codeshare.airline.identity.access.authentication.data;


import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.authentication.entities.OidcConfigEntity;
import com.codeshare.airline.identity.access.authentication.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.authentication.repository.OidcIdentityProviderRepository;
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

    @Transactional
    public void load(UUID tenantId) {

        log.info("🔹 Bootstrapping Identity Providers for {} tenants", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        bootstrapProvider(tenant, AuthSource.INTERNAL, 1);
        bootstrapProvider(tenant, AuthSource.AZURE, 2);
        bootstrapProvider(tenant, AuthSource.LDAP, 3);

        log.info("✅ Identity Provider bootstrap completed");
    }

    private void bootstrapProvider(
            Tenant tenant,
            AuthSource authSource,
            int priority
    ) {

        OidcIdentityProviderEntity provider =
                identityProviderRepository
                        .findByTenant_IdAndAuthSource(tenant.getId(), authSource)
                        .orElseGet(() -> createProvider(tenant, authSource, priority));

        if (authSource == AuthSource.INTERNAL || authSource == AuthSource.AZURE) {
            ensureOidcConfig(provider, authSource);
        } else {
            log.info("ℹ️ LDAP provider ready for tenant [{}]", tenant.getTenantCode());
        }
    }

    private OidcIdentityProviderEntity createProvider(
            Tenant tenant,
            AuthSource authSource,
            int priority
    ) {

        OidcIdentityProviderEntity provider =
                OidcIdentityProviderEntity.builder()
                        .tenant(tenant)
                        .authSource(authSource)
                        .enabled(authSource == AuthSource.INTERNAL || authSource == AuthSource.AZURE)
                        .priority(priority)
                        .build();

        identityProviderRepository.save(provider);

        log.info("➕ Created {} provider for tenant [{}]", authSource, tenant.getTenantCode());
        return provider;
    }

    private void ensureOidcConfig(
            OidcIdentityProviderEntity provider,
            AuthSource authSource
    ) {

        OidcConfigEntity existing = provider.getOidcConfig();

        // ✅ if already valid, skip
        if (existing != null && existing.getIssuerUri() != null) {
            return;
        }

        OidcConfigEntity config =
                authSource == AuthSource.INTERNAL
                        ? internalDefaults(provider)
                        : azureDefaults(provider);

        provider.setOidcConfig(config);
        identityProviderRepository.save(provider);

        log.info(
                "➕ Ensured {} OIDC config for tenant [{}]",
                authSource,
                provider.getTenant().getTenantCode()
        );
    }

    // ===============================
    // DEFAULT CONFIGS
    // ===============================

    private OidcConfigEntity internalDefaults(OidcIdentityProviderEntity provider) {

        return OidcConfigEntity.builder()
                .identityProvider(provider)
                .issuerUri("https://auth.codeshare.local")
                .authorizationUri("https://auth.codeshare.local/oauth2/authorize")
                .tokenUri("https://auth.codeshare.local/oauth2/token")
                .jwkSetUri("https://auth.codeshare.local/.well-known/jwks.json")
                .clientId("codeshare-internal-gateway")
                .clientSecretRef("vault:internal-oidc-gateway-secret")
                .redirectUri("https://app.codeshare.com/auth/oidc/callback")
                .grantType("authorization_code")
                .clientAuthMethod("client_secret_basic")
                .scopes("openid profile email")
                .enforceRedirectUri(false)
                .build();
    }

    private OidcConfigEntity azureDefaults(OidcIdentityProviderEntity provider) {

        return OidcConfigEntity.builder()
                .identityProvider(provider)
                .issuerUri("https://login.microsoftonline.com/{tenant-id}/v2.0")
                .authorizationUri("https://login.microsoftonline.com/{tenant-id}/oauth2/v2.0/authorize")
                .tokenUri("https://login.microsoftonline.com/{tenant-id}/oauth2/v2.0/token")
                .jwkSetUri("https://login.microsoftonline.com/common/discovery/v2.0/keys")
                .clientId("azure-gateway-id")
                .clientSecretRef("vault:azure-gateway-secret")
                .redirectUri("https://app.codeshare.com/auth/oidc/callback")
                .grantType("authorization_code")
                .clientAuthMethod("client_secret_post")
                .scopes("openid profile email offline_access")
                .enforceRedirectUri(true)
                .build();
    }

    // ===============================
    // TENANT-AWARE VALIDATION
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long count = identityProviderRepository.countByTenantId(tenantId);

        // INTERNAL + AZURE + LDAP = 3 providers expected
        return count >= 3;
    }
}
