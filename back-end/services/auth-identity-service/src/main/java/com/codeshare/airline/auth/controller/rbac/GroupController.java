package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.common.auth.identity.model.GroupDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
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
    public ResponseEntity<ServiceResponse<?>> create(@RequestBody GroupDTO dto) {

        log.info("→ Creating group for tenant {}", dto.getTenantId());

        return ResponseEntity.ok(
                ServiceResponse.success(groupService.create(dto))
        );
    }

    // ---------------------------------------------------------
    // UPDATE GROUP
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<?>> update(
            @PathVariable UUID id,
            @RequestBody GroupDTO dto
    ) {
        log.info("→ Updating group {} for tenant {}", id, dto.getTenantId());

        return ResponseEntity.ok(
                ServiceResponse.success(groupService.update(id, dto))
        );
    }

    // ---------------------------------------------------------
    // GET GROUP BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<?>> getById(@PathVariable UUID id) {

        log.debug("→ Fetching group {}", id);

        return ResponseEntity.ok(
                ServiceResponse.success(groupService.getById(id))
        );
    }

    // ---------------------------------------------------------
    // GET GROUPS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse<?>> getByTenant(@RequestParam UUID tenantId) {

        log.debug("→ Fetching groups for tenant {}", tenantId);

        return ResponseEntity.ok(
                ServiceResponse.success(groupService.getByTenant(tenantId))
        );
    }

    // ---------------------------------------------------------
    // DELETE GROUP
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<?>> delete(@PathVariable UUID id) {

        log.warn("→ Deleting group {}", id);

        groupService.delete(id);

        return ResponseEntity.ok(
                ServiceResponse.success(AppConstan.NO_DATA)
        );
    }
}
