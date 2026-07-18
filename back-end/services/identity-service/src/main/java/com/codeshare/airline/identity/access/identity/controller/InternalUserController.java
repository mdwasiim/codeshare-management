package com.codeshare.airline.identity.access.identity.controller;

import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final AuthUserService AuthUserService;

    // ---------------------------------------------------------------------
    // SOFT DELETE USER
    // (your service should implement soft delete using isDeleted flag)
    // ---------------------------------------------------------------------
    @GetMapping
    public List<AuthUserDTO> getAllUsers() {
        return AuthUserService.getAllUsers();
    }

}
