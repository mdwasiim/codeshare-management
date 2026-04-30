package com.codeshare.airline.identity.controller;

import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.identity.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserService authUserService;

    // ---------------------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------------------
    @PostMapping
    public AuthUserDTO create(@Valid @RequestBody AuthUserDTO dto) {
        return authUserService.create(dto);
    }

    // ---------------------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public AuthUserDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody AuthUserDTO dto) {
        return authUserService.update(id, dto);
    }

    // ---------------------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public  AuthUserDTO getById(@PathVariable UUID id) {
        return authUserService.getById(id);
    }

    // ---------------------------------------------------------------------
    // SOFT DELETE USER
    // (your service should implement soft delete using isDeleted flag)
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        authUserService.delete(id);
    }

    @GetMapping
    public List<AuthUserDTO> getAllUsers() {
        return authUserService.getAllUsers();
    }

}
