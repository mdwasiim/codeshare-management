package com.codeshare.airline.identity.access.identity.service;


import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;

import java.util.List;

public interface GroupService {

    GroupDTO create(GroupDTO dto);

    GroupDTO update(Long id, GroupDTO dto);

    GroupDTO getById(Long id);

    List<GroupDTO> getAll();

    void delete(Long id);

    void deleteByTenantGroupId(Long tenantGroupId);
}

