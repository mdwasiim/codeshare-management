package com.codeshare.airline.identity.utils.data;


import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.entities.UserGroup;
import com.codeshare.airline.identity.repository.GroupRepository;
import com.codeshare.airline.identity.repository.UserGroupRepository;
import com.codeshare.airline.identity.repository.UserRepository;
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
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public void loadUser(List<Tenant> tenants) {

        log.info("⏳ Bootstrapping internal admin users...");

        for (Tenant tenant : tenants) {
            createAdminIfMissing(tenant);
        }
    }

    private void createAdminIfMissing(Tenant tenant) {

        String username = "admin";
        String email = tenant.getTenantCode() + "@codeshare.com";

        boolean exists = userRepository.existsByUsername(username);

        if (exists) return;

        Group adminGroup = groupRepository
                .findByCodeAndTenant_TenantCode("ADMIN", tenant.getTenantCode())
                .orElseThrow(() -> new RuntimeException("Admin group not found"));

        // 🔥 create user FIRST
        User user = User.builder()
                .username(username.toLowerCase())
                .email(email)
                .firstName("Admin")
                .lastName(tenant.getName())
                .password(passwordEncoder.encode("admin"))
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

        User savedUser = userRepository.save(user);

        UserGroup userGroup = UserGroup.builder()
                .tenant(tenant)
                .user(savedUser)
                .group(adminGroup)
                .build();

        userGroupRepository.save(userGroup);
    }
}

