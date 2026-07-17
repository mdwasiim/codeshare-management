package com.codeshare.airline.identity.access.assignments.service;

import com.codeshare.airline.platform.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;

import java.util.List;

public interface GroupMenuAssignmentService {

    // =============================================
    // GET MENUS ASSIGNED TO GROUP
    // =============================================
    List<MenuDTO> getMenusByGroup(
            Long groupId
    );

    // =============================================
    // REPLACE GROUP MENUS
    // =============================================
    List<GroupMenuDTO> replaceGroupMenus(
            Long groupId,
            List<Long> menuIds
    );
}