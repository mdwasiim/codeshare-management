package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.entities.rbac.UserRole;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleLoader {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository repo;

    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ UserRoleLoader: User-role mappings already exist — skipping load.");
            return;
        }

        log.info("⏳ UserRoleLoader: Assigning roles to users for {} tenants...", tenantIds.size());

        int total = 0;

        for (String tenantIdStr : tenantIds) {

            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId == null) continue;

            total += assignRolesToUsers(tenantId);
        }

        log.info("✔ UserRoleLoader: Completed. {} user-role mappings created.", total);
    }

    private int assignRolesToUsers(UUID tenantId) {

        List<User> users = userRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (users.isEmpty()) {
            log.warn("⚠ No users found for tenant {} — skipping user-role mapping", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping user-role mapping", tenantId);
            return 0;
        }

        List<UserRole> mappings = new ArrayList<>();

        for (User user : users) {
            for (Role role : roles) {
                mappings.add(UserRole.builder()
                        .user(user)
                        .role(role)
                        .build());
            }
        }

        repo.saveAll(mappings);

        log.info("✔ Tenant {}: {} user-role mappings created.", tenantId, mappings.size());

        return mappings.size();
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid tenant UUID '{}' — skipping...", id);
            return null;
        }
    }
}

