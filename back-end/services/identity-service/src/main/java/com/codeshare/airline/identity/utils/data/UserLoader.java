package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.identity.repository.UserRepository;
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

    public void loadUser(UUID tenantId) {

        log.info("⏳ Bootstrapping internal users...");

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Tenant not found: " + tenantId
                        ));

        createUserIfMissing(
                tenant,
                "admin",
                "admin",
                "System",
                "Administrator"
        );

        createUserIfMissing(
                tenant,
                "auditor",
                "auditor",
                "Audit",
                "User"
        );
    }

    private void createUserIfMissing(
            Tenant tenant,
            String username,
            String password,
            String firstName,
            String lastName
    ) {

        boolean exists =
                userRepository.existsByUsernameAndTenant(
                        username,
                        tenant
                );

        if (exists) {

            log.info(
                    "User [{}] already exists for tenant [{}]",
                    username,
                    tenant.getTenantCode()
            );

            return;
        }

        String email =
                username.toLowerCase()
                        + "@"
                        + tenant.getTenantCode().toLowerCase()
                        + ".codeshare.com";

        User user = User.builder()

                .username(username.toLowerCase())
                .email(email)

                .firstName(firstName)
                .lastName(lastName)

                .password(
                        passwordEncoder.encode(password)
                )

                .enabled(true)
                .active(true)

                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)

                .authSource(AuthSource.INTERNAL)

                .externalId("internal:" + username)

                .recordStatus(RecordStatus.ACTIVE)

                .tenant(tenant)

                .build();

        userRepository.save(user);

        log.info(
                "✅ User [{}] created for tenant [{}]",
                username,
                tenant.getTenantCode()
        );
    }

    public boolean isLoaded(UUID tenantId) {

         return userRepository.count()>0;
    }
}