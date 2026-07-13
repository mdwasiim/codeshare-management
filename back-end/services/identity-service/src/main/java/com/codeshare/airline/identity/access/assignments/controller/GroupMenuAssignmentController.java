package com.codeshare.airline.identity.access.assignments.controller;

import com.codeshare.airline.platform.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.assignments.service.GroupMenuAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/group-menus")
@RequiredArgsConstructor
public class GroupMenuAssignmentController {

    private final GroupMenuAssignmentService service;

    // =====================================================
    // GET MENUS BY GROUP
    // =====================================================
    @GetMapping("/{groupId}")
    public List<MenuDTO> getMenusByGroup(
            @PathVariable Long groupId
    ) {

        log.info(
                "Fetching menus for group {}",
                groupId
        );

        return service.getMenusByGroup(groupId);
    }

    // =====================================================
    // REPLACE GROUP MENUS
    // =====================================================
    @PutMapping("/{groupId}")
    public List<GroupMenuDTO> replaceGroupMenus(
            @PathVariable Long groupId,
            @RequestBody List<Long> menuIds
    ) {

        log.info(
                "Replacing menus for group {} with {} menus",
                groupId,
                menuIds.size()
        );

        return service.replaceGroupMenus(
                groupId,
                menuIds
        );
    }
}