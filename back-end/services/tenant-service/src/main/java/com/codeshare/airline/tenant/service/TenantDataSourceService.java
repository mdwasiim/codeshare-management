package com.codeshare.airline.tenant.service;

import com.codeshare.airline.common.tenant.model.TenantDataSourceDTO;

import java.util.List;
import java.util.UUID;

public interface TenantDataSourceService {

    TenantDataSourceDTO create(TenantDataSourceDTO dto);

    TenantDataSourceDTO update(UUID id, TenantDataSourceDTO dto);

    TenantDataSourceDTO getById(UUID id);

    List<TenantDataSourceDTO> getAll();

    void delete(UUID id);
}
