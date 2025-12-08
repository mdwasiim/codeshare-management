package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.UserService;
import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ---------------------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(userService.create(dto)));
    }

    // ---------------------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(userService.update(id, dto)));
    }

    // ---------------------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(userService.getById(id)));
    }

    // ---------------------------------------------------------------------
    // GET USERS BY TENANT
    // (pagination-ready — you can enable Pageable later)
    // ---------------------------------------------------------------------
    @GetMapping("/by-tenant/{tenantId}")
    public ResponseEntity<ServiceResponse> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(ServiceResponse.success(userService.getByTenant(tenantId)));
    }

    // ---------------------------------------------------------------------
    // GET USERS BY ORGANIZATION
    // ---------------------------------------------------------------------
    @GetMapping("/by-organization/{organizationId}")
    public ResponseEntity<ServiceResponse> getByOrganization(@PathVariable UUID organizationId) {
        return ResponseEntity.ok(ServiceResponse.success(userService.getByOrganization(organizationId)));
    }

    // ---------------------------------------------------------------------
    // SOFT DELETE USER
    // (your service should implement soft delete using isDeleted flag)
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }

}
