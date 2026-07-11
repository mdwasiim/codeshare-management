package com.codeshare.airline.identity.access.identity.controller;

import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
    public List<AuthUserDTO> getAllUsers(HttpServletRequest req) {
        log.info("Authorization header = {}", req.getHeader("Authorization"));
        AuthUserService.getAllUsers();
        return AuthUserService.getAllUsers();
    }

}
