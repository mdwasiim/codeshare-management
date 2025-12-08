package com.codeshare.airline.tenant.service;

import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;

import java.util.List;
import java.util.UUID;

public interface TenantOrganizationService {

    TenantOrganizationDTO create(TenantOrganizationDTO tenantOrganizationDTO);

    TenantOrganizationDTO update(UUID id, TenantOrganizationDTO tenantOrganizationDTO);

    TenantOrganizationDTO getById(UUID id);

    List<TenantOrganizationDTO> getAllByTenant(UUID tenantId);

    void delete(UUID id);

}
