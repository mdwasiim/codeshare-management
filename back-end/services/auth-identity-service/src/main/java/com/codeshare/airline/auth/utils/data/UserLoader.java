package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserOrganization;
import com.codeshare.airline.auth.feign.client.TenantOrganizationclient;
import com.codeshare.airline.auth.repository.UserOrganizationRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader {

    private final UserRepository repo;
    private final UserOrganizationRepository userOrgRepo;
    private final PasswordEncoder passwordEncoder;
    private final TenantOrganizationclient tenantOrganizationclient;

    private static final List<String> DEFAULT_USERS = List.of("admin", "manager", "staff");

    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ UserLoader: Users already exist — skipping load.");
            return;
        }

        log.info("⏳ Creating users for {} tenants...", tenantIds.size());

        List<User> toSaveUsers = new ArrayList<>();
        Map<UUID, List<UUID>> tenantOrgMap = new HashMap<>();

        // --- Load organizations & create users ---
        for (String tenantIdStr : tenantIds) {

            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId == null) continue;

            // Fetch organizations dynamically from tenant-service
            List<UUID> orgIds = fetchOrgIds(tenantId);
            if (orgIds.isEmpty()) {
                log.warn("⚠ No organizations found for tenant {} — skipping user creation", tenantId);
                continue;
            }
            tenantOrgMap.put(tenantId, orgIds);

            // Create users per tenant
            for (String username : DEFAULT_USERS) {
                toSaveUsers.add(createUser(username, tenantId));
            }
        }

        // Save users
        List<User> savedUsers = repo.saveAll(toSaveUsers);

        // --- Create User → Organization mapping ---
        List<UserOrganization> mappings = new ArrayList<>();

        for (User user : savedUsers) {
            List<UUID> orgIds = tenantOrgMap.get(user.getTenantId());
            if (orgIds == null) continue;

            for (UUID orgId : orgIds) {
                mappings.add(UserOrganization.builder()
                        .user(user)
                        .organizationId(orgId)
                        .build());
            }
        }

        userOrgRepo.saveAll(mappings);

        log.info("✔ UserLoader: {} users created and {} user-org links added.",
                savedUsers.size(), mappings.size());
    }

    // Create user object
    private User createUser(String username, UUID tenantId) {
        return User.builder()
                .username(username)
                .firstName(cap(username))
                .lastName("User")
                .email(username + "@example.com")
                .password(passwordEncoder.encode("admin"))
                .tenantId(tenantId)
                .enabled(true)
                .accountNonLocked(true)
                .build();
    }

    // Fetch organizations for a tenant using Feign
    private List<UUID> fetchOrgIds(UUID tenantId) {
        try {
            List<TenantOrganizationDTO> orgs = tenantOrganizationclient.getOrganizations(tenantId);
            return orgs.stream().map(TenantOrganizationDTO::getId).toList();
        } catch (Exception ex) {
            log.error("❌ Failed to fetch organizations for tenant {}: {}", tenantId, ex.getMessage(), ex);
            return List.of();
        }
    }

    private UUID safeUUID(String id) {
        try { return UUID.fromString(id); }
        catch (Exception e) {
            log.warn("⚠ Invalid tenant UUID '{}'", id);
            return null;
        }
    }

    private String cap(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
