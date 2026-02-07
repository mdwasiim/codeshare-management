package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.OidcConfigEntity;
import com.codeshare.airline.auth.entities.OidcIdentityProviderEntity;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.OidcIdentityProviderRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import com.codeshare.airline.core.enums.AuthSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OidcIdentityProviderDataLoader {

    private final TenantRepository tenantRepository;
    private final OidcIdentityProviderRepository identityProviderRepository;

    @Transactional
    public void load(List<UUID> tenantIds) {

        log.info("🔹 Bootstrapping Identity Providers for {} tenants", tenantIds.size());

        for (UUID tenantId : tenantIds) {

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            bootstrapProvider(tenant, AuthSource.INTERNAL, 1);
            bootstrapProvider(tenant, AuthSource.AZURE, 2);
            bootstrapProvider(tenant, AuthSource.LDAP, 3);
        }

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
            log.info("ℹ️ LDAP provider ready for ssim [{}]", tenant.getTenantCode());
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
                        .enabled(authSource == AuthSource.INTERNAL)
                        .priority(priority)
                        .build();

        identityProviderRepository.save(provider);

        log.info("➕ Created {} provider for ssim [{}]", authSource, tenant.getTenantCode());
        return provider;
    }

    private void ensureOidcConfig(
            OidcIdentityProviderEntity provider,
            AuthSource authSource
    ) {

        if (provider.getOidcConfig() != null) {
            return;
        }

        OidcConfigEntity config =
                authSource == AuthSource.INTERNAL
                        ? internalDefaults(provider)
                        : azureDefaults(provider);

        provider.setOidcConfig(config);
        identityProviderRepository.save(provider);

        log.info(
                "➕ Created {} OIDC config for ssim [{}]",
                authSource,
                provider.getTenant().getTenantCode()
        );
    }

    /* ================= DEFAULT CONFIGS ================= */

    private OidcConfigEntity internalDefaults(OidcIdentityProviderEntity provider) {

        return OidcConfigEntity.builder()
                .identityProvider(provider)
                .issuerUri("https://auth.codeshare.local")
                .authorizationUri("https://auth.codeshare.local/oauth2/authorize")
                .tokenUri("https://auth.codeshare.local/oauth2/token")
                .jwkSetUri("https://auth.codeshare.local/.well-known/jwks.json")
                .clientId("codeshare-internal-client")
                .clientSecretRef("vault:internal-oidc-client-secret")
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
                .issuerUri("https://login.microsoftonline.com/{ssim-id}/v2.0")
                .authorizationUri("https://login.microsoftonline.com/{ssim-id}/oauth2/v2.0/authorize")
                .tokenUri("https://login.microsoftonline.com/{ssim-id}/oauth2/v2.0/token")
                .jwkSetUri("https://login.microsoftonline.com/common/discovery/v2.0/keys")
                .clientId("azure-client-id")
                .clientSecretRef("vault:azure-client-secret")
                .redirectUri("https://app.codeshare.com/auth/oidc/callback")
                .grantType("authorization_code")
                .clientAuthMethod("client_secret_post")
                .scopes("openid profile email offline_access")
                .enforceRedirectUri(true)
                .build();
    }
}
