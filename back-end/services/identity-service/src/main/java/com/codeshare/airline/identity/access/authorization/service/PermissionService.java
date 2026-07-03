package com.codeshare.airline.identity.access.authorization.service;


import com.codeshare.airline.core.dto.tenant.PermissionDTO;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    PermissionDTO create(PermissionDTO dto);

    PermissionDTO update(UUID id, PermissionDTO dto);

    PermissionDTO getById(UUID id);

    List<PermissionDTO> getAllTenant();

    void delete(UUID id);
}
