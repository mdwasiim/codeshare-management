package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.GroupDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // ---------------------------------------------------------
    // CREATE GROUP
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<CSMServiceResponse<?>> create(@RequestBody GroupDTO dto) {

        log.info("→ Creating group for tenant {}", dto.getTenantId());

        return ResponseEntity.ok(
                CSMServiceResponse.success(groupService.create(dto))
        );
    }

    // ---------------------------------------------------------
    // UPDATE GROUP
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse<?>> update(
            @PathVariable UUID id,
            @RequestBody GroupDTO dto
    ) {
        log.info("→ Updating group {} for tenant {}", id, dto.getTenantId());

        return ResponseEntity.ok(
                CSMServiceResponse.success(groupService.update(id, dto))
        );
    }

    // ---------------------------------------------------------
    // GET GROUP BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse<?>> getById(@PathVariable UUID id) {

        log.debug("→ Fetching group {}", id);

        return ResponseEntity.ok(
                CSMServiceResponse.success(groupService.getById(id))
        );
    }

    // ---------------------------------------------------------
    // GET GROUPS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<CSMServiceResponse<?>> getByTenant(@RequestParam UUID tenantId) {

        log.debug("→ Fetching groups for tenant {}", tenantId);

        return ResponseEntity.ok(
                CSMServiceResponse.success(groupService.getByTenant(tenantId))
        );
    }

    // ---------------------------------------------------------
    // DELETE GROUP
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse<?>> delete(@PathVariable UUID id) {

        log.warn("→ Deleting group {}", id);

        groupService.delete(id);

        return ResponseEntity.ok(
                CSMServiceResponse.success(CSMConstants.NO_DATA)
        );
    }
}
