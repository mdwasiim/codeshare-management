package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.GroupRoleAssignmentService;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/group-role")
@RequiredArgsConstructor
public class GroupRoleAssignmentController {

    private final GroupRoleAssignmentService service;

    // ---------------------------------------------------------
    // ASSIGN ROLE → GROUP
    // ---------------------------------------------------------
    @PostMapping("/{groupId}/{roleId}")
    public ResponseEntity<ServiceResponse<?>> assign(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {
        log.info("→ Assigning role {} to group {}", roleId, groupId);

        return ResponseEntity.ok(
                ServiceResponse.success(
                        service.assignRoleToGroup(groupId, roleId)
                )
        );
    }

    // ---------------------------------------------------------
    // REMOVE ROLE FROM GROUP
    // ---------------------------------------------------------
    @DeleteMapping("/{groupId}/{roleId}")
    public ResponseEntity<ServiceResponse<?>> remove(
            @PathVariable UUID groupId,
            @PathVariable UUID roleId
    ) {
        log.info("→ Removing role {} from group {}", roleId, groupId);

        service.removeRoleFromGroup(groupId, roleId);

        return ResponseEntity.ok(
                ServiceResponse.success(AppConstan.NO_DATA)
        );
    }

    // ---------------------------------------------------------
    // GET ROLES ASSIGNED TO GROUP
    // ---------------------------------------------------------
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ServiceResponse<?>> getRolesByGroup(@PathVariable UUID groupId) {

        log.debug("→ Fetching roles for group {}", groupId);

        return ResponseEntity.ok(
                ServiceResponse.success(
                        service.getRolesByGroup(groupId)
                )
        );
    }

    // ---------------------------------------------------------
    // GET GROUPS ASSIGNED TO ROLE
    // ---------------------------------------------------------
    @GetMapping("/role/{roleId}")
    public ResponseEntity<ServiceResponse<?>> getGroupsByRole(@PathVariable UUID roleId) {

        log.debug("→ Fetching groups for role {}", roleId);

        return ResponseEntity.ok(
                ServiceResponse.success(
                        service.getGroupsByRole(roleId)
                )
        );
    }
}
