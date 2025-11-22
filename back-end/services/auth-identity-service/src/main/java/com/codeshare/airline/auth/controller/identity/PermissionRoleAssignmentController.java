package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.PermissionRoleAssignmentService;
import com.codeshare.airline.common.auth.model.PermissionRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permission-role")
public class PermissionRoleAssignmentController {

    private final PermissionRoleAssignmentService  service;

    @Autowired
    public PermissionRoleAssignmentController(PermissionRoleAssignmentService service) {
        this.service = service;
    }

    // assign permission to role
    @PostMapping("/{roleId}/{permissionId}")
    public ResponseEntity<PermissionRoleDTO> assign(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        return ResponseEntity.ok(service.assignPermissionToRole(roleId, permissionId));
    }

    // remove permission from role
    @DeleteMapping("/{roleId}/{permissionId}")
    public ResponseEntity<Void> remove(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        service.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }

    // get permissions assigned to a role
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionRoleDTO>> getByRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(service.getPermissionsByRole(roleId));
    }

    // get roles that have a permission
    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<PermissionRoleDTO>> getByPermission(@PathVariable UUID permissionId) {
        return ResponseEntity.ok(service.getRolesByPermission(permissionId));
    }
}
