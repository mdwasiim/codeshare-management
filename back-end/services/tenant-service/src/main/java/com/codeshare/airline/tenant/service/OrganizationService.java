package com.codeshare.airline.tenant.service;

import com.codeshare.airline.common.tenant.model.OrganizationDTO;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationDTO create(OrganizationDTO organizationDTO);

    OrganizationDTO update(UUID id, OrganizationDTO organizationDTO);

    OrganizationDTO getById(UUID id);

    List<OrganizationDTO> getAllByTenant(UUID tenantId);

    void delete(UUID id);

}
