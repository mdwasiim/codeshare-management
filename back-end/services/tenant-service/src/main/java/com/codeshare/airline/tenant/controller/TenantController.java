package com.codeshare.airline.tenant.management.controller;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantLoginOptionDTO;
import com.codeshare.airline.platform.core.dto.tenant.IdentityProviderConfigDTO;
import com.codeshare.airline.platform.core.dto.tenant.OidcConfigDTO;
import com.codeshare.airline.tenant.api.response.TenantIdentityProviderRowResponse;
import com.codeshare.airline.tenant.api.response.TenantOidcConfigRowResponse;
import com.codeshare.airline.tenant.common.ExactFilter;
import com.codeshare.airline.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/tenants")
    public TenantDTO create(@RequestBody TenantDTO dto) {
        return tenantService.create(dto);
    }

    @PutMapping("/tenants/{id}")
    public TenantDTO update(@PathVariable Long id, @RequestBody TenantDTO dto) {
        return tenantService.update(id, dto);
    }

    @GetMapping("/tenants/{id}")
    public TenantDTO getById(@PathVariable Long id) {
        return tenantService.getById(id);
    }

    @GetMapping("/tenants/code/{code}")
    public TenantDTO getByCode(@PathVariable String code) {
        return tenantService.getByCode(code);
    }

    @GetMapping("/tenants/code/{code}/auth-context")
    public TenantAuthContextDTO getPublicAuthContext(@PathVariable String code) {
        return tenantService.getAuthContextByCode(code);
    }

    @GetMapping("/tenants")
    public List<TenantDTO> getAll(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(tenantService.getAll(), filters);
    }

    @GetMapping("/internal/tenants")
    public List<TenantDTO> getAllInternal(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(tenantService.getAll(), filters);
    }

    @GetMapping("/tenant-identity-providers")
    public List<TenantIdentityProviderRowResponse> getIdentityProviders(@RequestParam Map<String, String> filters) {
        List<TenantIdentityProviderRowResponse> rows = tenantService.getAll().stream()
                .map(this::toIdentityProviderRow)
                .toList();

        return ExactFilter.apply(rows, filters);
    }

    @GetMapping("/tenant-oidc-configs")
    public List<TenantOidcConfigRowResponse> getOidcConfigs(@RequestParam Map<String, String> filters) {
        List<TenantOidcConfigRowResponse> rows = tenantService.getAll().stream()
                .map(this::toOidcConfigRow)
                .toList();

        return ExactFilter.apply(rows, filters);
    }

    @GetMapping("/tenants/login-options")
    public List<TenantLoginOptionDTO> getLoginOptions() {
        return tenantService.getLoginOptions();
    }

    @DeleteMapping("/tenants/{id}")
    public String delete(@PathVariable Long id) {
        tenantService.delete(id);
        return CSMConstants.NO_DATA;
    }

    @GetMapping("/internal/tenants/code/{code}/auth-context")
    public TenantAuthContextDTO getAuthContext(@PathVariable String code) {
        return tenantService.getAuthContextByCode(code);
    }

    private TenantIdentityProviderRowResponse toIdentityProviderRow(TenantDTO tenant) {
        IdentityProviderConfigDTO provider = resolveProvider(tenant);
        OidcConfigDTO oidcConfig = resolveOidcConfig(tenant, provider);

        return new TenantIdentityProviderRowResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getTenantCode(),
                resolveAuthSource(tenant, provider),
                oidcConfig == null ? null : oidcConfig.getIssuerUri(),
                oidcConfig == null ? null : oidcConfig.getClientId(),
                oidcConfig == null ? null : oidcConfig.getRedirectUri(),
                tenant.getStatus()
        );
    }

    private TenantOidcConfigRowResponse toOidcConfigRow(TenantDTO tenant) {
        IdentityProviderConfigDTO provider = resolveProvider(tenant);
        OidcConfigDTO oidcConfig = resolveOidcConfig(tenant, provider);

        return new TenantOidcConfigRowResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getTenantCode(),
                resolveAuthSource(tenant, provider),
                oidcConfig == null ? null : oidcConfig.getIssuerUri(),
                oidcConfig == null ? null : oidcConfig.getAuthorizationUri(),
                oidcConfig == null ? null : oidcConfig.getTokenUri(),
                oidcConfig == null ? null : oidcConfig.getJwkSetUri(),
                oidcConfig == null ? null : oidcConfig.getScopes()
        );
    }

    private IdentityProviderConfigDTO resolveProvider(TenantDTO tenant) {
        if (tenant.getIdentityProviders() == null || tenant.getIdentityProviders().isEmpty()) {
            return null;
        }

        return tenant.getIdentityProviders().stream()
                .filter(provider -> provider.getOidcConfig() != null)
                .findFirst()
                .orElse(tenant.getIdentityProviders().getFirst());
    }

    private OidcConfigDTO resolveOidcConfig(TenantDTO tenant, IdentityProviderConfigDTO provider) {
        if (provider != null && provider.getOidcConfig() != null) {
            return provider.getOidcConfig();
        }

        return tenant.getOidcConfig();
    }

    private String resolveAuthSource(TenantDTO tenant, IdentityProviderConfigDTO provider) {
        if (provider != null && provider.getAuthSource() != null) {
            return provider.getAuthSource().name();
        }

        return tenant.getAuthSource() == null ? null : tenant.getAuthSource().name();
    }
}
