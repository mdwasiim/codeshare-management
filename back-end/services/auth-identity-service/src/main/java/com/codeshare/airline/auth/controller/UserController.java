package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.AuthUserService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserService AuthUserService;

    // ---------------------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<CSMServiceResponse> create(@Valid @RequestBody AuthUserDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(AuthUserService.create(dto)));
    }

    // ---------------------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AuthUserDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(AuthUserService.update(id, dto)));
    }

    // ---------------------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CSMServiceResponse.success(AuthUserService.getById(id)));
    }

    // ---------------------------------------------------------------------
    // SOFT DELETE USER
    // (your service should implement soft delete using isDeleted flag)
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> delete(@PathVariable UUID id) {
        AuthUserService.delete(id);
        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }

}
