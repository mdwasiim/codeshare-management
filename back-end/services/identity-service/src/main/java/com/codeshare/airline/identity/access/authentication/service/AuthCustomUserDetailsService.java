package com.codeshare.airline.identity.access.authentication.service;

import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = resolveUser(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .accountLocked(user.isAccountNonLocked())
                .accountExpired(user.isAccountNonExpired())
                .credentialsExpired(user.isCredentialsNonExpired())
                .build();
    }

    private java.util.Optional<User> resolveUser(String username) {
        try {
            TenantContext tenant = TenantContextHolder.getTenant();
            return userRepository.findByUsernameAndTenantId(username, tenant.getId());
        } catch (IllegalStateException ex) {
            return userRepository.findByUsername(username);
        }
    }
}
