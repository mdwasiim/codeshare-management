package com.codeshare.airline.identity.access.assignments.controller;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.dto.tenant.UserGroupDTO;
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
}