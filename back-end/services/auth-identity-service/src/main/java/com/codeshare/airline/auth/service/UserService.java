package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.identity.model.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO create(UserDTO dto);

    UserDTO update(UUID id, UserDTO dto);

    UserDTO getById(UUID id);

    List<UserDTO> getByTenant(UUID tenantId);

    List<UserDTO> getByOrganization(UUID orgId);

    void delete(UUID id);

    UserDTO getByUsername(String name);
}
