package com.codeshare.airline.identity.access.assignments.controller;

import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.access.assignments.service.GroupRoleAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/group-role")
@RequiredArgsConstructor
public class GroupRoleAssignmentController {

    private final GroupRoleAssignmentService service;

    // ---------------------------------------------------------
    // ASSIGN ROLE → GROUP
    // ---------------------------------------------------------
    @PostMapping("/{groupId}/{roleId}")
    public GroupRoleDTO assign(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {

        log.info(
                "Assigning role {} to group {}",
                roleId,
                groupId
        );

        return service.assignRoleToGroup(
                groupId,
                roleId
        );
    }

    // ---------------------------------------------------------
    // REMOVE ROLE FROM GROUP
    // ---------------------------------------------------------
    @DeleteMapping("/{groupId}/{roleId}")
    public void remove(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {

        log.info(
                "Removing role {} from group {}",
                roleId,
                groupId
        );

        service.removeRoleFromGroup(
                groupId,
                roleId
        );
    }

    // ---------------------------------------------------------
    // GET ROLES ASSIGNED TO GROUP
    // ---------------------------------------------------------
    @GetMapping("/role/{groupId}")
    public List<RoleDTO> getRolesByGroup(
            @PathVariable UUID groupId
    ) {

        log.debug(
                "Fetching roles for group {}",
                groupId
        );

        return service.getRolesByGroup(groupId);
    }

    // ---------------------------------------------------------
    // REPLACE GROUP ROLES
    // ---------------------------------------------------------
    @PutMapping("/role/{groupId}")
    public List<GroupRoleDTO> replaceGroupRoles(
            @PathVariable UUID groupId,
            @RequestBody List<UUID> roleIds
    ) {

        log.info(
                "Replacing roles for group {} with {} roles",
                groupId,
                roleIds.size()
        );

        return service.replaceGroupRoles(
                groupId,
                roleIds
        );
    }

    // ---------------------------------------------------------
    // GET GROUPS ASSIGNED TO ROLE
    // ---------------------------------------------------------
    @GetMapping("/group/{roleId}")
    public List<GroupRoleDTO> getGroupsByRole(
            @PathVariable UUID roleId
    ) {

        log.debug(
                "Fetching groups for role {}",
                roleId
        );

        return service.getGroupsByRole(roleId);
    }
}