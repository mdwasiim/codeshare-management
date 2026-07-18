package com.codeshare.airline.tenant.api.response;

public record TenantOidcConfigRowResponse(
        Long id,
        String name,
        String tenantCode,
        String authSource,
        String issuerUri,
        String authorizationUri,
        String tokenUri,
        String jwkSetUri,
        String scopes
) {
}
