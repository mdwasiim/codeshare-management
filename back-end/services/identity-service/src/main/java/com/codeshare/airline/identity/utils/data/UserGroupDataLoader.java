package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.entities.UserGroup;
import com.codeshare.airline.identity.repository.GroupRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.identity.repository.UserGroupRepository;
import com.codeshare.airline.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupDataLoader {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    /**
     * Default group assignment
     */
    private static final String DEFAULT_GROUP = "VIEWER_GROUP";

    @Transactional
    public void load(List<UUID> tenantIds) {

        log.info("🔹 Bootstrapping UserGroup mappings for {} tenants", tenantIds.size());

        for (UUID tenantId : tenantIds) {
            processTenant(tenantId);
        }

        log.info("✅ UserGroup bootstrap completed");
    }

    private void processTenant(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<User> users = userRepository.findAllByTenant_Id(tenant.getId());

        if (users.isEmpty()) {
            log.warn("⚠ No users found for tenant {}", tenant.getTenantCode());
            return;
        }

        Group defaultGroup = groupRepository
                .findByCodeAndTenant_TenantCode(DEFAULT_GROUP, tenant.getTenantCode())
                .orElseThrow(() ->
                        new IllegalStateException("Default group not found: " + DEFAULT_GROUP));

        // 🔥 load existing mappings once
        Set<String> existing = userGroupRepository.findMappings(tenant);

        for (User user : users) {

            // skip admin (already mapped in UserLoader)
            if ("admin".equalsIgnoreCase(user.getUsername())) {
                continue;
            }

            String key = user.getId() + ":" + defaultGroup.getId();

            if (existing.contains(key)) continue;

            userGroupRepository.save(
                    UserGroup.builder()
                            .tenant(tenant)
                            .user(user)
                            .group(defaultGroup)
                            .build()
            );

            log.debug("➕ Assigned [{}] to user [{}]",
                    DEFAULT_GROUP, user.getUsername());
        }
    }
}