package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.UserSeed;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
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
    private final IdentityBootstrapData bootstrapData;
    private final HostAirlineTenantClient tenantClient;

    public void loadUser(UUID tenantId) {
        log.info("Bootstrapping internal users for tenant {}", tenantId);

        Set<String> existingUsernames = userRepository.findByTenantId(tenantId).stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        String tenantCode = resolveTenantCode(tenantId);
        bootstrapData.users().forEach(seed -> createUserIfMissing(tenantId, tenantCode, seed, existingUsernames));
    }

    private void createUserIfMissing(UUID tenantId, String tenantCode, UserSeed seed, Set<String> existingUsernames) {
        String username = seed.username().toLowerCase();
        if (existingUsernames.contains(username)) {
            log.info("User [{}] already exists for tenant [{}]", username, tenantCode);
            return;
        }

        existingUsernames.add(username);
        String email = resolveBootstrapEmail(tenantCode, seed, username);

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
                .tenantId(tenantId)
                .build();

        userRepository.save(user);
        log.info("User [{}] created for tenant [{}]", username, tenantCode);
    }

    private String resolveBootstrapEmail(String tenantCode, UserSeed seed, String username) {
        String tenantEmail = username + "@" + tenantCode.toLowerCase() + ".codeshare.com";
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
        long actual = userRepository.findAllByTenantId(tenantId).size();
        return actual >= expected;
    }

    private String resolveTenantCode(UUID tenantId) {
        return tenantClient.getAll().stream()
                .filter(tenant -> tenantId.equals(tenant.getId()))
                .map(tenant -> tenant.getTenantCode())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tenant not found in tenant-service: " + tenantId));
    }
}
