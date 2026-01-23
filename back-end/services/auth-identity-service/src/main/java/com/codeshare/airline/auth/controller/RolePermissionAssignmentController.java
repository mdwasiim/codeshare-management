package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionAssignmentController {

    private final RolePermissionAssignmentService service;

    // ==============================================================
    // ASSIGN ONE PERMISSION → ONE ROLE
    // ==============================================================
    @PostMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<CSMServiceResponse> assignPermissionToRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        log.info("Assigning permission {} → role {}", permissionId, roleId);

        RolePermissionDTO result = service.assignPermissionToRole(roleId, permissionId);

        return ResponseEntity.ok(CSMServiceResponse.success(result));
    }

    // ==============================================================
    // REMOVE PERMISSION FROM ROLE
    // ==============================================================
    @DeleteMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<CSMServiceResponse> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        log.info("Removing permission {} from role {}", permissionId, roleId);

        service.removePermissionFromRole(roleId, permissionId);

        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }

    // ==============================================================
    // GET PERMISSIONS ASSIGNED TO A ROLE
    // ==============================================================
    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<CSMServiceResponse> getPermissionsByRole(@PathVariable UUID roleId) {

        log.info("Fetching permissions for role {}", roleId);

        List<RolePermissionDTO> list = service.getPermissionsByRole(roleId);

        return ResponseEntity.ok(CSMServiceResponse.success(list));
    }

    // ==============================================================
    // GET ROLES THAT HAVE A GIVEN PERMISSION
    // ==============================================================
    @GetMapping("/permission/{permissionId}/roles")
    public ResponseEntity<CSMServiceResponse> getRolesByPermission(@PathVariable UUID permissionId) {

        log.info("Fetching roles assigned to permission {}", permissionId);

        List<RolePermissionDTO> list = service.getRolesByPermission(permissionId);

        return ResponseEntity.ok(CSMServiceResponse.success(list));
    }

    // ==============================================================
    // OPTIONAL — BULK ASSIGNMENT: Assign Multiple Permissions to a Role
    // ==============================================================
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<CSMServiceResponse> assignMultiplePermissions(
            @PathVariable UUID roleId,
            @RequestBody List<UUID> permissionIds
    ) {
        log.info("Bulk assigning {} permissions → role {}", permissionIds.size(), roleId);

        List<RolePermissionDTO> created = service.assignPermissionsToRole(roleId, permissionIds);

        return ResponseEntity.ok(CSMServiceResponse.success(created));
    }
}
