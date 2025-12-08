package com.codeshare.airline.auth.controller.internal;

import com.codeshare.airline.auth.service.UserGroupService;
import com.codeshare.airline.common.auth.identity.model.TenantGroupUserSyncDTO;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/group-users")
@RequiredArgsConstructor
public class UserGroupSyncController {

    private final UserGroupService service;

    @PostMapping("/assign")
    public ResponseEntity<ServiceResponse> assignViaSync(@RequestBody TenantGroupUserSyncDTO dto) {
        service.assignUserViaSync(dto.getTenantGroupId(), dto.getUserId());
        return ResponseEntity.ok(ServiceResponse.success("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<ServiceResponse> removeViaSync(@RequestBody TenantGroupUserSyncDTO dto) {
        service.removeUserViaSync(dto.getTenantGroupId(), dto.getUserId());
        return ResponseEntity.ok(ServiceResponse.success("OK"));
    }
}

