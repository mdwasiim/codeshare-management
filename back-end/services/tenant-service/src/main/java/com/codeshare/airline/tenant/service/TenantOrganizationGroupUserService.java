package com.codeshare.airline.tenant.service;


import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupUserDTO;

import java.util.List;
import java.util.UUID;

public interface TenantOrganizationGroupUserService {

    TenantOrganizationGroupUserDTO assignUserToGroup(TenantOrganizationGroupUserDTO dto);

    void removeUserFromGroup(UUID groupId, UUID userId);

    List<TenantOrganizationGroupUserDTO> getUsersByGroup(UUID groupId);

    List<TenantOrganizationGroupUserDTO> getGroupsByUser(UUID userId);
}
