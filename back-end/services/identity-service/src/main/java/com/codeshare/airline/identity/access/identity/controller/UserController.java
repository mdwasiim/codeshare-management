package com.codeshare.airline.identity.access.identity.controller;

import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserService authUserService;

    // ---------------------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------------------
    @PostMapping
    public AuthUserDTO create(@RequestBody AuthUserDTO dto) {
        return authUserService.create(dto);
    }

    // ---------------------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public AuthUserDTO update(
            @PathVariable Long id,
            @Valid @RequestBody AuthUserDTO dto) {
        return authUserService.update(id, dto);
    }

    // ---------------------------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public  AuthUserDTO getById(@PathVariable Long id) {
        return authUserService.getById(id);
    }

    // ---------------------------------------------------------------------
    // SOFT DELETE USER
    // (your service should implement soft delete using isDeleted flag)
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        authUserService.delete(id);
    }

    @GetMapping
    public List<AuthUserDTO> getAllUsers() {
        return authUserService.getAllUsers();
    }

}
