package com.codeshare.airline.identity.access.identity.controller;

import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.identity.access.identity.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // ---------------------------------------------------------
    // CREATE GROUP
    // ---------------------------------------------------------
    @PostMapping
    public GroupDTO create(@RequestBody GroupDTO dto) {

        log.info("→ Creating group for ingestion {}", dto.getTenantId());

        return groupService.create(dto);
    }

    // ---------------------------------------------------------
    // UPDATE GROUP
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public GroupDTO update(
            @PathVariable UUID id,
            @RequestBody GroupDTO dto
    ) {
        log.info("→ Updating group {} for ingestion {}", id, dto.getTenantId());

        return groupService.update(id, dto);
    }

    // ---------------------------------------------------------
    // GET GROUP BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public GroupDTO getById(@PathVariable UUID id) {

        log.debug("→ Fetching group {}", id);

        return groupService.getById(id);
    }

    // ---------------------------------------------------------
    // GET GROUPS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public List<GroupDTO> getAll() {
        return groupService.getAll();
    }

    // ---------------------------------------------------------
    // DELETE GROUP
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        log.warn("→ Deleting group {}", id);

        groupService.delete(id);

    }
}
