package com.codeshare.airline.identity.access.assignments.service;

import com.codeshare.airline.platform.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;

import java.util.List;
import java.util.UUID;

public interface GroupMenuAssignmentService {

    // =============================================
    // GET MENUS ASSIGNED TO GROUP
    // =============================================
    List<MenuDTO> getMenusByGroup(
            UUID groupId
    );

    // =============================================
    // REPLACE GROUP MENUS
    // =============================================
    List<GroupMenuDTO> replaceGroupMenus(
            UUID groupId,
            List<UUID> menuIds
    );
}