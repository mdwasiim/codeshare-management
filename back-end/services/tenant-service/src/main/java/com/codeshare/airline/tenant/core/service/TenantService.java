package com.codeshare.airline.tenant.core.service;

import com.codeshare.airline.core.dto.tenant.TenantAuthContextDTO;
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

    TenantAuthContextDTO getAuthContextByCode(String tenantCode);
}
