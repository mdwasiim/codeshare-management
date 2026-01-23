package com.codeshare.airline.auth.authentication.service.core;

import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.auth.authentication.exception.TenantResolutionException;
import com.codeshare.airline.auth.authentication.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.auth.service.TenantService;
import com.codeshare.airline.core.enums.AuthSource;
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
            throw new TenantResolutionException("Invalid or inactive tenant: " + tenantCode);
        }
        return tenant;
    }

    public IdentityProviderConfig resolveAuthType(TenantContext tenant) {
        return tenantIdentityProviderSelector.select(tenant, null);
    }

    /**
     * Used by JWT filter to validate tenant from token
     */
    public void validateTenant(String tenantCode) {
        resolveTenant(tenantCode);
    }

}
