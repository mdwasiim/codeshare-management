package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.GroupRoleAssignmentService;
import com.codeshare.airline.common.auth.model.GroupRoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/group-role")
public class GroupRoleAssignmentController {

    private final GroupRoleAssignmentService service;

    public GroupRoleAssignmentController(GroupRoleAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/{groupId}/{roleId}")
    public ResponseEntity<GroupRoleDTO> assign(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {
        return ResponseEntity.ok(service.assignRoleToGroup(groupId, roleId));
    }

    @DeleteMapping("/{groupId}/{roleId}")
    public ResponseEntity<Void> remove(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {
        service.removeRoleFromGroup(groupId, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupRoleDTO>> getRoles(@PathVariable UUID groupId) {
        return ResponseEntity.ok(service.getRolesByGroup(groupId));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<GroupRoleDTO>> getGroups(@PathVariable UUID roleId) {
        return ResponseEntity.ok(service.getGroupsByRole(roleId));
    }
}

