package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    // -------------------------------------------------------------------------
    // Load user by username for Spring Security authentication
    // -------------------------------------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert user roles → Spring Security authorities
        String[] authorities = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())        // encoded password
                .authorities(authorities)            // role names mapped to GrantedAuthority
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
