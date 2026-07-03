package com.codeshare.airline.identity.access.authentication.core.service.core;

import com.codeshare.airline.identity.access.authentication.core.security.SecurityUtils;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    public UserContextService(SecurityUtils securityUtils,
                              UserRepository userRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = securityUtils.getCurrentUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}