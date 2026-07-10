package com.codeshare.airline.identity.access.identity.service;

import com.codeshare.airline.identity.access.identity.entities.Tenant;

public interface TenantService {

    Tenant getTenantByTenantCode(String tenantCode);
}
