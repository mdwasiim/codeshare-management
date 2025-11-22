package com.codeshare.airline.tenant.service;



import com.codeshare.airline.common.tenant.model.UserGroupDTO;

import java.util.List;
import java.util.UUID;

public interface UserGroupService {

    UserGroupDTO create(UserGroupDTO dto);

    UserGroupDTO update(UUID id, UserGroupDTO dto);

    UserGroupDTO getById(UUID id);

    List<UserGroupDTO> getAll(UUID tenantId);

    void delete(UUID id);
}
