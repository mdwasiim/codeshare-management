package com.codeshare.airline.tenant.api.response;

import com.codeshare.airline.platform.core.enums.common.TenantStatus;

public record TenantIdentityProviderRowResponse(
        Long id,
        String name,
        String tenantCode,
        String authSource,
        String issuerUri,
        String clientId,
        String redirectUri,
        TenantStatus status
) {
}
