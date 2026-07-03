package com.codeshare.airline.identity.access.authentication.core.domain;

public final class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    private TenantContextHolder() {}

    public static void setTenant(TenantContext tenant) {
        CONTEXT.set(tenant);
    }

    public static TenantContext getTenant() {
        TenantContext tenant = CONTEXT.get();
        if (tenant == null) {
            throw new IllegalStateException("Tenant not resolved for request");
        }
        return tenant;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
