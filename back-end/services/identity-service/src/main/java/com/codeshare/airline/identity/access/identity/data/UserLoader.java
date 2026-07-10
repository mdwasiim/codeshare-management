package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.UserSeed;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void loadUser(UUID tenantId) {
        log.info("Bootstrapping internal users for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingUsernames = userRepository.findByTenant(tenant).stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        bootstrapData.users().forEach(seed -> createUserIfMissing(tenant, seed, existingUsernames));
    }

    private void createUserIfMissing(Tenant tenant, UserSeed seed, Set<String> existingUsernames) {
        String username = seed.username().toLowerCase();
        if (existingUsernames.contains(username)) {
            log.info("User [{}] already exists for tenant [{}]", username, tenant.getTenantCode());
            return;
        }

        existingUsernames.add(username);
        String email = resolveBootstrapEmail(tenant, seed, username);

        User user = User.builder()
                .username(username)
                .email(email)
                .firstName(seed.firstName())
                .lastName(seed.lastName())
                .password(passwordEncoder.encode(seed.password()))
                .enabled(seed.enabled())
                .active(seed.enabled())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authSource(seed.authSource())
                .externalId("internal:" + username)
                .recordStatus(RecordStatus.ACTIVE)
                .tenant(tenant)
                .build();

        userRepository.save(user);
        log.info("User [{}] created for tenant [{}]", username, tenant.getTenantCode());
    }

    private String resolveBootstrapEmail(Tenant tenant, UserSeed seed, String username) {
        String tenantEmail = username + "@" + tenant.getTenantCode().toLowerCase() + ".codeshare.com";
        if (seed.email() == null || seed.email().isBlank()) {
            return tenantEmail;
        }

        String normalized = seed.email().trim().toLowerCase();
        if (normalized.endsWith(".codeshare.com")) {
            return tenantEmail;
        }

        return seed.email();
    }

    public boolean isLoaded(UUID tenantId) {
        long expected = bootstrapData.users().size();
        long actual = userRepository.findAllByTenant_Id(tenantId).size();
        return actual >= expected;
    }
}
