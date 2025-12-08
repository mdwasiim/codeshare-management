package com.codeshare.airline.tenant.conbtroller;

import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupUserDTO;
import com.codeshare.airline.tenant.service.TenantOrganizationGroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-group-users")
@RequiredArgsConstructor
public class TenantOrganizationGroupUserController {

    private final TenantOrganizationGroupUserService service;

    @PostMapping
    public ResponseEntity<ServiceResponse> assign(@RequestBody TenantOrganizationGroupUserDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.assignUserToGroup(dto)));
    }

    @DeleteMapping("/{groupId}/{userId}")
    public ResponseEntity<ServiceResponse> remove(
            @PathVariable UUID groupId,
            @PathVariable UUID userId
    ) {
        service.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<ServiceResponse> getUsers(@PathVariable UUID groupId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getUsersByGroup(groupId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ServiceResponse> getGroups(@PathVariable UUID userId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getGroupsByUser(userId)));
    }
}
