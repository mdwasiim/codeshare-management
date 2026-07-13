package com.codeshare.airline.tenant.service;

import com.codeshare.airline.platform.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantLoginOptionDTO;

import java.util.List;

public interface TenantService {

    TenantDTO create(TenantDTO dto);

    TenantDTO update(Long id, TenantDTO dto);

    TenantDTO getById(Long id);

    TenantDTO getByCode(String code);

    List<TenantDTO> getAll();

    List<TenantLoginOptionDTO> getLoginOptions();

    void delete(Long id);

    TenantAuthContextDTO getAuthContextByCode(String tenantCode);
}
