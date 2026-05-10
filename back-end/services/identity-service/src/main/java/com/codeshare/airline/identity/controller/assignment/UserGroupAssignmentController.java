package com.codeshare.airline.identity.controller.assignment;

import com.codeshare.airline.core.dto.tenant.GroupDTO;
import com.codeshare.airline.core.dto.tenant.UserGroupDTO;
import com.codeshare.airline.identity.service.assignment.UserGroupAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            @PathVariable UUID userId
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
            @PathVariable UUID userId,
            @RequestBody List<UUID> groupIds
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