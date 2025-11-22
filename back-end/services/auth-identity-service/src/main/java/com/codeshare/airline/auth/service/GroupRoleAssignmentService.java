package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.model.GroupRoleDTO;

import java.util.List;
import java.util.UUID;

public interface GroupRoleAssignmentService {

    GroupRoleDTO assignRoleToGroup(UUID groupId, UUID roleId);

    void removeRoleFromGroup(UUID groupId, UUID roleId);

    List<GroupRoleDTO> getRolesByGroup(UUID groupId);

    List<GroupRoleDTO> getGroupsByRole(UUID roleId);
}
