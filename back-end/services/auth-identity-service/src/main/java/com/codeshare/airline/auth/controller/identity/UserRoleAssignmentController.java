package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.UserRoleAssignmentService;
import com.codeshare.airline.common.auth.model.UserRoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-role")
public class UserRoleAssignmentController {

    private final UserRoleAssignmentService service;

    public UserRoleAssignmentController(UserRoleAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/{userId}/{roleId}")
    public ResponseEntity<UserRoleDTO> assign(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {
        return ResponseEntity.ok(service.assignRoleToUser(userId, roleId));
    }

    @DeleteMapping("/{userId}/{roleId}")
    public ResponseEntity<Void> remove(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {
        service.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleDTO>> getRoles(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getRolesByUser(userId));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserRoleDTO>> getUsers(@PathVariable UUID roleId) {
        return ResponseEntity.ok(service.getUsersByRole(roleId));
    }
}
