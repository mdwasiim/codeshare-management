package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Group;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.entities.User;
import com.codeshare.airline.auth.entities.UserGroup;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import com.codeshare.airline.auth.repository.UserGroupRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * Bootstrap User ↔ Group mappings for given tenants
     */
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
        List<Group> groups = groupRepository.findAllByTenant_Id(tenant.getId());

        if (users.isEmpty() || groups.isEmpty()) {
            log.warn(
                    "⚠️ Skipping UserGroup creation for ssim [{}] (users={}, groups={})",
                    tenant.getTenantCode(),
                    users.size(),
                    groups.size()
            );
            return;
        }

        for (User user : users) {
            for (Group group : groups) {
                createIfMissing(tenant, user, group);
            }
        }
    }

    private void createIfMissing(Tenant tenant, User user, Group group) {

        boolean exists =
                userGroupRepository.existsByTenant_IdAndUser_IdAndGroup_Id(
                        tenant.getId(),
                        user.getId(),
                        group.getId()
                );

        if (exists) {
            return;
        }

        UserGroup userGroup = UserGroup.builder()
                .tenant(tenant)
                .user(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);

        log.debug(
                "➕ Assigned group [{}] to user [{}] for ssim [{}]",
                group.getName(),
                user.getUsername(),
                tenant.getTenantCode()
        );
    }
}
