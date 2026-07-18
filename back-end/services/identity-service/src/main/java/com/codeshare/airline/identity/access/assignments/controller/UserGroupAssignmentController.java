package com.codeshare.airline.identity.access.assignments.controller;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.identity.access.assignments.service.UserGroupAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-groups")
@RequiredArgsConstructor
public class UserGroupAssignmentController {

    private final UserGroupAssignmentService service;

    // =====================================================
    // GET GROUPS ASSIGNED TO USER
    // =====================================================
    @GetMapping("/group/{userId}")
    public List<GroupDTO> getGroupsByUser(
            @PathVariable Long userId
    ) {

        log.info(
                "Fetching groups for user {}",
                userId
        );

        return service.getGroupsByUser(userId);
    }

    // =====================================================
    // GET USERS ASSIGNED TO GROUP
    // =====================================================
    @GetMapping("/user/{groupId}")
    public List<AuthUserDTO> getUsersByGroup(
            @PathVariable Long groupId
    ) {

        log.info(
                "Fetching users for group {}",
                groupId
        );

        return service.getUsersByGroup(groupId);
    }

    // =====================================================
    // REPLACE USER GROUPS
    // =====================================================
    @PutMapping("/group/{userId}")
    public List<UserGroupDTO> replaceUserGroups(
            @PathVariable Long userId,
            @RequestBody List<Long> groupIds
    ) {

        log.info(
                "Replacing groups for user {} with {} groups",
                userId,
                groupIds.size()
        );

        return service.replaceUserGroups(
                userId,
                groupIds
        );
    }

    // =====================================================
    // REPLACE GROUP USERS
    // =====================================================
    @PutMapping("/user/{groupId}")
    public List<UserGroupDTO> replaceGroupUsers(
            @PathVariable Long groupId,
            @RequestBody List<Long> userIds
    ) {

        log.info(
                "Replacing users for group {} with {} users",
                groupId,
                userIds == null ? 0 : userIds.size()
        );

        return service.replaceGroupUsers(
                groupId,
                userIds
        );
    }
}
