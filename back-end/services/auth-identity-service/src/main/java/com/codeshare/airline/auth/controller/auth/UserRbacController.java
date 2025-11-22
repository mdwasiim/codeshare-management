package com.codeshare.airline.auth.controller.auth;


import com.codeshare.airline.auth.model.UserRbacResponse;
import com.codeshare.airline.auth.service.UserRbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-rbac")
public class UserRbacController {

    private final UserRbacService service;

    @Autowired
    public UserRbacController(UserRbacService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserRbacResponse> resolve(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.resolveUserRbac(userId));
    }

    @PostMapping("/{userId}/invalidate")
    public ResponseEntity<Void> invalidate(@PathVariable UUID userId) {
        service.invalidateUserRbacCache(userId);
        return ResponseEntity.noContent().build();
    }
}
