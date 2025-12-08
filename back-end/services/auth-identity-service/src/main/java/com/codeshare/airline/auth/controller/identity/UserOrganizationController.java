package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.UserOrganizationService;
import com.codeshare.airline.common.auth.identity.model.UserOrganizationDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-organizations")
@RequiredArgsConstructor
public class UserOrganizationController {

    private final UserOrganizationService userOrganizationService;

    // -------------------------------------------------------------
    // Assign a user to an organization
    // -------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> assignUserToOrg(@RequestBody UserOrganizationDTO dto) {
        return ResponseEntity.ok(
                ServiceResponse.success(userOrganizationService.assignUserToOrganization(dto))
        );
    }

    // -------------------------------------------------------------
    // Get all organizations linked to a user
    // -------------------------------------------------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<ServiceResponse> getOrganizationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(
                ServiceResponse.success(userOrganizationService.getOrganizationsByUser(userId))
        );
    }

    // -------------------------------------------------------------
    // Get all users under an organization
    // -------------------------------------------------------------
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ServiceResponse> getUsersByOrganization(@PathVariable UUID organizationId) {
        return ResponseEntity.ok(
                ServiceResponse.success(userOrganizationService.getUsersByOrganization(organizationId))
        );
    }

    // -------------------------------------------------------------
    // Remove a user from an organization
    // -------------------------------------------------------------
    @DeleteMapping("/{mappingId}")
    public ResponseEntity<ServiceResponse> removeUserFromOrg(@PathVariable UUID mappingId) {
        userOrganizationService.removeUserFromOrganization(mappingId);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
