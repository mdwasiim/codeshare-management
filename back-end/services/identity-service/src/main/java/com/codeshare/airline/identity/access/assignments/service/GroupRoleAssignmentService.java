package com.codeshare.airline.identity.access.assignments.service;

import com.codeshare.airline.platform.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.platform.core.dto.tenant.RoleDTO;

import java.util.List;

public interface GroupRoleAssignmentService {

    GroupRoleDTO assignRoleToGroup(
            Long groupId,
            Long roleId
    );

    void removeRoleFromGroup(
            Long groupId,
            Long roleId
    );

    List<RoleDTO> getRolesByGroup(
            Long groupId
    );

    List<GroupRoleDTO> getGroupsByRole(
            Long roleId
    );

    // =====================================================
    // REPLACE GROUP ROLES
    // =====================================================
    List<GroupRoleDTO> replaceGroupRoles(
            Long groupId,
            List<Long> roleIds
    );
}