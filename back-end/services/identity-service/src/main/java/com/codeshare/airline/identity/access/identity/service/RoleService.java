package com.codeshare.airline.identity.access.identity.service;



import com.codeshare.airline.platform.core.dto.tenant.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO create(RoleDTO dto);

    RoleDTO update(Long id, RoleDTO dto);

    void delete(Long id);

    RoleDTO getById(Long id);


    List<RoleDTO> getAllRoles();

}
