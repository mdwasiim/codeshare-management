package com.codeshare.airline.auth.controller.auth;

import com.codeshare.airline.auth.service.UserRbacService;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-rbac")
@RequiredArgsConstructor
public class UserRbacController {

    private final UserRbacService service;

    // ---------------------------------------------------------
    // FETCH RBAC PERMISSIONS FOR A USER
    // ---------------------------------------------------------
    @GetMapping("/{userId}")
    public ResponseEntity<ServiceResponse> resolve(@PathVariable UUID userId) {
        return ResponseEntity.ok(ServiceResponse.success(service.resolveUserRbac(userId)));
    }

    // ---------------------------------------------------------
    // INVALIDATE RBAC CACHE FOR A USER
    // ---------------------------------------------------------
    @PostMapping("/{userId}/invalidate")
    public ResponseEntity<ServiceResponse> invalidate(@PathVariable UUID userId) {
        service.invalidateUserRbacCache(userId);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
