package com.codeshare.airline.auth.controller.internal;

import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.common.auth.identity.model.TenantGroupSyncDTO;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/groups")
@RequiredArgsConstructor
public class GroupSyncInernalController {

    private final GroupService service;

    @PostMapping("/sync")
    public ResponseEntity<ServiceResponse> sync(@RequestBody TenantGroupSyncDTO dto) {
        return ResponseEntity.ok(
                ServiceResponse.success(service.syncTenantGroup(dto))
        );
    }

    @DeleteMapping("/{tenantGroupId}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID tenantGroupId) {
        service.deleteByTenantGroupId(tenantGroupId);
        return ResponseEntity.ok(ServiceResponse.success("OK"));
    }
}

