package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.entities.User;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.core.enums.AuthSource;
import com.codeshare.airline.core.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void loadUser(List<Tenant> tenants) {

        log.info("⏳ Bootstrapping internal admin users...");

        for (Tenant tenant : tenants) {
            createAdminIfMissing(tenant);
        }
    }

    private void createAdminIfMissing(Tenant tenant) {

        String username = "admin";
        String email = tenant.getTenantCode()+ "@codeshare.com";

        boolean exists =
                userRepository.existsByUsername(
                        username
                );

        if (exists) {
            log.info(
                    "✔ Admin user '{}' already exists for tenant '{}'. Skipping.",
                    username,
                    tenant.getTenantCode()
            );
            return;
        }

        // ⚠️ Ideally inject from config or generate randomly
        String rawPassword = "admin";

        User user = User.builder()
                .username(username.toLowerCase())
                .email(email)
                .firstName("Admin")
                .lastName(tenant.getName())
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .active(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authSource(AuthSource.INTERNAL)
                .externalId("internal:" + username)
                .status(UserStatus.ACTIVE)
                .tenant(tenant)
                .build();

        userRepository.save(user);

        log.warn(
                "⚠️ Admin user '{}' created for tenant '{}'. Password must be changed immediately.",
                username,
                tenant.getTenantCode()
        );
    }
}

