package com.codeshare.airline.identity.access.assignments.controller;

import com.codeshare.airline.platform.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role-permissions")
@RequiredArgsConstructor
public class RolePermissionAssignmentController {

    private final RolePermissionAssignmentService service;

    // ==============================================================
    // GET ALL PERMISSIONS ASSIGNED TO ROLE
    // ==============================================================
    @GetMapping("/{roleId}")
    public List<RolePermissionDTO> getPermissionsByRole(
            @PathVariable Long roleId
    ) {

        log.info(
                "Fetching permissions for role {}",
                roleId
        );

        return service.getPermissionsByRole(roleId);
    }

    // ==============================================================
    // REPLACE ROLE PERMISSIONS
    // ==============================================================
    @PutMapping("/{roleId}")
    public List<RolePermissionDTO>  replaceRolePermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds
    ) {

        log.info(
                "Replacing permissions for role {} with {} permissions",
                roleId,
                permissionIds.size()
        );


        return service.replaceRolePermissions(
                roleId,
                permissionIds
        );
    }

    // ==============================================================
    // GET ROLES BY PERMISSION
    // ==============================================================
    @GetMapping("/permission/{permissionId}")
    public List<RolePermissionDTO>  getRolesByPermission(
            @PathVariable Long permissionId
    ) {

        log.info(
                "Fetching roles assigned to permission {}",
                permissionId
        );


        return service.getRolesByPermission(permissionId);
    }
}