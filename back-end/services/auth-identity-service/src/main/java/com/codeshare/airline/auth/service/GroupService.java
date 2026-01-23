package com.codeshare.airline.auth.service;



import com.codeshare.airline.core.dto.tenant.GroupDTO;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    GroupDTO create(GroupDTO dto);

    GroupDTO update(UUID id, GroupDTO dto);

    GroupDTO getById(UUID id);

    List<GroupDTO> getByTenant(UUID tenantId);

    void delete(UUID id);

    void deleteByTenantGroupId(UUID tenantGroupId);
}

