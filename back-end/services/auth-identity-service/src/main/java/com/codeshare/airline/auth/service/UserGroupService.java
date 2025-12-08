package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.identity.model.UserGroupDTO;

import java.util.List;
import java.util.UUID;

public interface UserGroupService {

    UserGroupDTO assignUser(UUID userId, UUID groupId);

    void removeUser(UUID userId, UUID groupId);

    List<UserGroupDTO> getGroupsByUser(UUID userId);

    List<UserGroupDTO> getUsersByGroup(UUID groupId);

    // For syncing with tenant-service
    void assignUserViaSync(UUID tenantGroupId, UUID userId);

    void removeUserViaSync(UUID tenantGroupId, UUID userId);
}
