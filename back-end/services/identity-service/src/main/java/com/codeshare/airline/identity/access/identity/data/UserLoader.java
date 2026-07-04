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

import java.util.UUID;

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

        bootstrapData.users().forEach(seed -> createUserIfMissing(tenant, seed));
    }

    private void createUserIfMissing(Tenant tenant, UserSeed seed) {
        String username = seed.username().toLowerCase();
        boolean exists = userRepository.existsByUsernameAndTenant(username, tenant);
        if (exists) {
            log.info("User [{}] already exists for tenant [{}]", username, tenant.getTenantCode());
            return;
        }

        String email = seed.email() == null || seed.email().isBlank()
                ? username + "@" + tenant.getTenantCode().toLowerCase() + ".codeshare.com"
                : seed.email();

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

    public boolean isLoaded(UUID tenantId) {
        long expected = bootstrapData.users().size();
        long actual = userRepository.findAllByTenant_Id(tenantId).size();
        return actual >= expected;
    }
}
