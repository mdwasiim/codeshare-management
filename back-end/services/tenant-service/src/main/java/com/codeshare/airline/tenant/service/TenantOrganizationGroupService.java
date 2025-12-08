package com.codeshare.airline.tenant.service;

import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupDTO;

import java.util.List;
import java.util.UUID;

public interface TenantOrganizationGroupService {

    TenantOrganizationGroupDTO create(TenantOrganizationGroupDTO dto);

    TenantOrganizationGroupDTO update(UUID id, TenantOrganizationGroupDTO dto);

    TenantOrganizationGroupDTO getById(UUID id);

    List<TenantOrganizationGroupDTO> getByTenant(UUID tenantId);

    List<TenantOrganizationGroupDTO> getByOrganization(UUID organizationId);

    void delete(UUID id);
}
