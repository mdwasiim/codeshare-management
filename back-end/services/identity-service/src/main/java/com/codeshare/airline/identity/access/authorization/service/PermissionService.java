package com.codeshare.airline.identity.access.authorization.service;


import com.codeshare.airline.platform.core.dto.tenant.PermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO create(PermissionDTO dto);

    PermissionDTO update(Long id, PermissionDTO dto);

    PermissionDTO getById(Long id);

    List<PermissionDTO> getAllTenant();

    void delete(Long id);
}
