package com.codeshare.airline.identity.authentication.service.core;

import com.codeshare.airline.identity.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.authentication.domain.TenantContext;
import com.codeshare.airline.identity.authentication.domain.TenantContextHolder;
import com.codeshare.airline.identity.authentication.exception.TenantResolutionException;
import com.codeshare.airline.identity.authentication.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TenantContextResolver {

    private final TenantService tenantService;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;

    public TenantContext resolveTenant(String tenantCode) {

        TenantContext tenant = tenantService.getByTenantCode(tenantCode);

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
}
