package com.codeshare.airline.tenant.service;


import com.codeshare.airline.common.tenant.model.TenantDTO;

import java.util.List;
import java.util.UUID;

public interface TenantService {

    TenantDTO create(TenantDTO dto);

    TenantDTO update(UUID id, TenantDTO dto);

    TenantDTO getById(UUID id);

    TenantDTO getByCode(String code);

    List<TenantDTO> getAll();

    void delete(UUID id);
}
