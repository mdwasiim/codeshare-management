package com.codeshare.airline.auth.service;


import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.core.dto.tenant.TenantDTO;

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
}
