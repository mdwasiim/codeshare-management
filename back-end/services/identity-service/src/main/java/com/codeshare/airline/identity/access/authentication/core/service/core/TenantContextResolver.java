package com.codeshare.airline.identity.access.authentication.core.service.core;

import com.codeshare.airline.platform.core.dto.tenant.IdentityProviderConfigDTO;
import com.codeshare.airline.platform.core.dto.tenant.OidcConfigDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.exception.TenantResolutionException;
import com.codeshare.airline.identity.access.authentication.core.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TenantContextResolver {

    private final HostAirlineTenantClient tenantClient;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;

    public TenantContext resolveTenant(String tenantCode) {

        TenantAuthContextDTO tenantDto = tenantClient.getAuthContext(tenantCode);
        TenantContext tenant = toTenantContext(tenantDto);

        if (tenant == null) {
            throw new TenantResolutionException("Invalid or inactive ingestion: " + tenantCode);
        }

        TenantContextHolder.setTenant(tenant); // 🔥 STORE IT
        return tenant;
    }

    public IdentityProviderConfig resolveAuthType(TenantContext tenant) {
        return tenantIdentityProviderSelector.select(tenant, null);
    }

    public void validateTenant(String tenantCode) {
        resolveTenant(tenantCode);
    }

    private TenantContext toTenantContext(TenantAuthContextDTO dto) {
        if (dto == null) {
            return null;
        }
        return TenantContext.builder()
                .id(dto.getId())
                .name(dto.getName())
                .tenantCode(dto.getTenantCode())
                .status(dto.getStatus())
                .logoUrl(dto.getLogoUrl())
                .region(dto.getRegion())
                .identityProviders(dto.getIdentityProviders() == null ? java.util.List.of() : dto.getIdentityProviders().stream().map(this::toIdentityProviderConfig).toList())
                .build();
    }

    private IdentityProviderConfig toIdentityProviderConfig(IdentityProviderConfigDTO dto) {
        return IdentityProviderConfig.builder()
                .authSource(dto.getAuthSource())
                .enabled(dto.isEnabled())
                .priority(dto.getPriority())
                .providerId(dto.getProviderId())
                .oidcConfig(toOidcConfig(dto.getOidcConfig()))
                .build();
    }

    private OidcConfig toOidcConfig(OidcConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        return OidcConfig.builder()
                .issuerUri(dto.getIssuerUri())
                .authorizationUri(dto.getAuthorizationUri())
                .tokenUri(dto.getTokenUri())
                .jwkSetUri(dto.getJwkSetUri())
                .clientId(dto.getClientId())
                .clientSecretRef(dto.getClientSecretRef())
                .redirectUri(dto.getRedirectUri())
                .scopes(dto.getScopes())
                .enforceRedirectUri(dto.isEnforceRedirectUri())
                .build();
    }
}
