package com.codeshare.airline.auth.service;


import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;

import java.util.List;
import java.util.UUID;

public interface GroupRoleAssignmentService {

    GroupRoleDTO assignRoleToGroup(UUID groupId, UUID roleId);

    void removeRoleFromGroup(UUID groupId, UUID roleId);

    List<GroupRoleDTO> getRolesByGroup(UUID groupId);

    List<GroupRoleDTO> getGroupsByRole(UUID roleId);
}
