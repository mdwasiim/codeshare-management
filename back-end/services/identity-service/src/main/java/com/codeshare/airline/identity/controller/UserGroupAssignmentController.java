package com.codeshare.airline.identity.controller;

import com.codeshare.airline.core.dto.tenant.GroupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-groups")
@RequiredArgsConstructor
public class UserGroupAssignmentController {

    @GetMapping("/{userId}")
    public List<GroupDTO> getGroupsByUser(
            @PathVariable UUID userId
    ){

    }

    @PutMapping("/{userId}")
    public List<UserGroupDTO> replaceUserGroups(
            @PathVariable UUID userId,
            @RequestBody List<UUID> groupIds
    ){


    }

}
