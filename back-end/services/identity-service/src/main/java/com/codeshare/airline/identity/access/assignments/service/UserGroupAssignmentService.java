package com.codeshare.airline.identity.access.assignments.service;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.dto.tenant.UserGroupDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserGroupAssignmentService {

    Set<String> resolveGroupCodes(UUID userId);

    // =============================================
    // GET GROUPS ASSIGNED TO USER
    // =============================================
    List<GroupDTO> getGroupsByUser(
            UUID userId
    );

    // =============================================
    // REPLACE USER GROUPS
    // =============================================
    List<UserGroupDTO> replaceUserGroups(
            UUID userId,
            List<UUID> groupIds);
}