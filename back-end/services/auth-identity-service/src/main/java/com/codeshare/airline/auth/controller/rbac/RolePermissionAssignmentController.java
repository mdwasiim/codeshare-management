package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.PermissionRoleAssignmentService;
import com.codeshare.airline.common.auth.identity.model.PermissionRoleDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
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

    private final PermissionRoleAssignmentService service;

    // ==============================================================
    // ASSIGN ONE PERMISSION → ONE ROLE
    // ==============================================================
    @PostMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<ServiceResponse> assignPermissionToRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        log.info("Assigning permission {} → role {}", permissionId, roleId);

        PermissionRoleDTO result = service.assignPermissionToRole(roleId, permissionId);

        return ResponseEntity.ok(ServiceResponse.success(result));
    }

    // ==============================================================
    // REMOVE PERMISSION FROM ROLE
    // ==============================================================
    @DeleteMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<ServiceResponse> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        log.info("Removing permission {} from role {}", permissionId, roleId);

        service.removePermissionFromRole(roleId, permissionId);

        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }

    // ==============================================================
    // GET PERMISSIONS ASSIGNED TO A ROLE
    // ==============================================================
    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<ServiceResponse> getPermissionsByRole(@PathVariable UUID roleId) {

        log.info("Fetching permissions for role {}", roleId);

        List<PermissionRoleDTO> list = service.getPermissionsByRole(roleId);

        return ResponseEntity.ok(ServiceResponse.success(list));
    }

    // ==============================================================
    // GET ROLES THAT HAVE A GIVEN PERMISSION
    // ==============================================================
    @GetMapping("/permission/{permissionId}/roles")
    public ResponseEntity<ServiceResponse> getRolesByPermission(@PathVariable UUID permissionId) {

        log.info("Fetching roles assigned to permission {}", permissionId);

        List<PermissionRoleDTO> list = service.getRolesByPermission(permissionId);

        return ResponseEntity.ok(ServiceResponse.success(list));
    }

    // ==============================================================
    // OPTIONAL — BULK ASSIGNMENT: Assign Multiple Permissions to a Role
    // ==============================================================
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<ServiceResponse> assignMultiplePermissions(
            @PathVariable UUID roleId,
            @RequestBody List<UUID> permissionIds
    ) {
        log.info("Bulk assigning {} permissions → role {}", permissionIds.size(), roleId);

        List<PermissionRoleDTO> created = service.assignPermissionsToRole(roleId, permissionIds);

        return ResponseEntity.ok(ServiceResponse.success(created));
    }
}
