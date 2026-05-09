package com.codeshare.airline.identity.service;


import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;

import java.util.List;
import java.util.UUID;

public interface GroupRoleAssignmentService {

    GroupRoleDTO assignRoleToGroup(UUID groupId, UUID roleId);

    void removeRoleFromGroup(UUID groupId, UUID roleId);

    List<RoleDTO> getRolesByGroup(UUID groupId);

    List<GroupRoleDTO> getGroupsByRole(UUID roleId);
}
