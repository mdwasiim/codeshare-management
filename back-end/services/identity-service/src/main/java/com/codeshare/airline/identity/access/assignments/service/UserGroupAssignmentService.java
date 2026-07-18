package com.codeshare.airline.identity.access.assignments.service;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;

import java.util.List;
import java.util.Set;

public interface UserGroupAssignmentService {

    Set<String> resolveGroupCodes(Long userId);

    // =============================================
    // GET GROUPS ASSIGNED TO USER
    // =============================================
    List<GroupDTO> getGroupsByUser(
            Long userId
    );

    // =============================================
    // GET USERS ASSIGNED TO GROUP
    // =============================================
    List<AuthUserDTO> getUsersByGroup(
            Long groupId
    );

    // =============================================
    // REPLACE USER GROUPS
    // =============================================
    List<UserGroupDTO> replaceUserGroups(
            Long userId,
            List<Long> groupIds);

    // =============================================
    // REPLACE GROUP USERS
    // =============================================
    List<UserGroupDTO> replaceGroupUsers(
            Long groupId,
            List<Long> userIds);
}
