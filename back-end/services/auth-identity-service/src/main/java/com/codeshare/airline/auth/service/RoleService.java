package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.identity.model.RoleDTO;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleDTO create(RoleDTO dto);

    RoleDTO update(UUID id, RoleDTO dto);

    void delete(UUID id);

    RoleDTO getById(UUID id);

    List<RoleDTO> getAllByTenant(UUID tenantId);

    List<RoleDTO> getAll();
}
