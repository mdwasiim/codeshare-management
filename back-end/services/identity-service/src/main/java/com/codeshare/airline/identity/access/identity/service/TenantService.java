package com.codeshare.airline.identity.access.identity.service;


import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.identity.entities.Tenant;

import java.util.List;
import java.util.UUID;

public interface TenantService {

    TenantDTO create(TenantDTO dto);

    TenantDTO update(UUID id, TenantDTO dto);

    TenantDTO getById(UUID id);

    TenantDTO getByCode(String code);

    List<TenantDTO> getAll();

    void delete(UUID id);

    TenantContext getByTenantCode(String tenantCode);

    Tenant getTenantByTenantCode(String tenantCode);
}
