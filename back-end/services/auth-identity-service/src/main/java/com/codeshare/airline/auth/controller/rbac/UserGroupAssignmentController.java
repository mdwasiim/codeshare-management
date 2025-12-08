package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.UserGroupService;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
public class UserGroupAssignmentController {

    private final UserGroupService service;

    // ==============================================================
    // ASSIGN USER → GROUP
    // ==============================================================
    @PostMapping("/{userId}/group/{groupId}")
    public ResponseEntity<ServiceResponse> assignUserToGroup(
            @PathVariable UUID userId,
            @PathVariable UUID groupId
    ) {
        log.info("Assigning user {} → group {}", userId, groupId);

        return ResponseEntity.ok(
                ServiceResponse.success(service.assignUser(userId, groupId))
        );
    }

    // ==============================================================
    // REMOVE USER FROM GROUP
    // ==============================================================
    @DeleteMapping("/{userId}/group/{groupId}")
    public ResponseEntity<ServiceResponse> removeUserFromGroup(
            @PathVariable UUID userId,
            @PathVariable UUID groupId
    ) {
        log.info("Removing user {} from group {}", userId, groupId);

        service.removeUser(userId, groupId);

        return ResponseEntity.ok(
                ServiceResponse.success(AppConstan.NO_DATA)
        );
    }

    // ==============================================================
    // GET GROUPS ASSIGNED TO USER
    // ==============================================================
    @GetMapping("/{userId}/groups")
    public ResponseEntity<ServiceResponse> getGroupsByUser(@PathVariable UUID userId) {

        log.info("Fetching groups for user {}", userId);

        return ResponseEntity.ok(
                ServiceResponse.success(service.getGroupsByUser(userId))
        );
    }

    // ==============================================================
    // GET USERS IN A GROUP
    // ==============================================================
    @GetMapping("/group/{groupId}/users")
    public ResponseEntity<ServiceResponse> getUsersByGroup(@PathVariable UUID groupId) {

        log.info("Fetching users for group {}", groupId);

        return ResponseEntity.ok(
                ServiceResponse.success(service.getUsersByGroup(groupId))
        );
    }
}
