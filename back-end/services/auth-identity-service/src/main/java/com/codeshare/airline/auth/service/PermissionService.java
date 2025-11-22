package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.model.PermissionDTO;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    PermissionDTO create(PermissionDTO dto);

    PermissionDTO update(UUID id, PermissionDTO dto);

    PermissionDTO getById(UUID id);

    List<PermissionDTO> getByTenant(UUID tenantId);

    void delete(UUID id);
}
