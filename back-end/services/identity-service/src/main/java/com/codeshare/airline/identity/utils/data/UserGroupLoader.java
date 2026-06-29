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

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final TenantRepository tenantRepository;

    // =========================
    // USER -> GROUP MAPPINGS
    // =========================
    private static final Map<String, List<String>> USER_GROUP_MAP =
            Map.ofEntries(

                    Map.entry(
                            "admin",
                            List.of("PLATFORM_TEAM")
                    ),

                    Map.entry(
                            "auditor",
                            List.of("AUDIT_TEAM")
                    )
            );

    @Transactional
    public void load(UUID tenantId) {

        log.info("⏳ UserGroupLoader: assigning users to groups...");

        assignUserGroups(tenantId);

        log.info("✅ UserGroupLoader: completed.");
    }

    private void assignUserGroups(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Tenant not found: " + tenantId
                        ));

        List<User> users =
                userRepository.findByTenant(tenant);

        List<Group> groups =
                groupRepository.findByTenant(tenant);

        if (users.isEmpty() || groups.isEmpty()) {
            log.warn(
                    " Skipping tenant [{}] — users or groups missing",
                    tenant.getTenantCode()
            );

            return;
        }

        Set<String> existingMappings =
                userGroupRepository.findMappingsByTenantId(tenant.getId());

        List<UserGroup> toSave =
                new ArrayList<>();

        Map<String, Group> groupByCode =
                new HashMap<>();

        for (Group group : groups) {
            groupByCode.put(group.getCode(), group);
        }

        for (User user : users) {

            List<String> allowedGroups =
                    USER_GROUP_MAP.getOrDefault(
                            user.getUsername(),
                            List.of("DEFAULT_USERS")
                    );

            for (String groupCode : allowedGroups) {

                Group group =
                        groupByCode.get(groupCode);

                if (group == null) {

                    log.warn(
                            "⚠ Group [{}] not found for user [{}]",
                            groupCode,
                            user.getUsername()
                    );

                    continue;
                }

                saveUserGroup(
                        tenant,
                        user,
                        group,
                        existingMappings,
                        toSave
                );
            }
        }

        if (!toSave.isEmpty()) {

            userGroupRepository.saveAll(toSave);

            log.info(
                    "✅ Tenant [{}]: {} user-group mappings created.",
                    tenant.getTenantCode(),
                    toSave.size()
            );

        } else {

            log.info(
                    "✅ Tenant [{}]: user-group mappings already exist.",
                    tenant.getTenantCode()
            );
        }
    }

    private void saveUserGroup(
            Tenant tenant,
            User user,
            Group group,
            Set<String> existingMappings,
            List<UserGroup> toSave
    ) {

        String key =
                user.getUsername() + ":" + group.getCode();

        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);

        toSave.add(
                UserGroup.builder()
                        .tenant(tenant)
                        .user(user)
                        .group(group)
                        .build()
        );
    }

    // =========================
    // VALIDATION
    // =========================
    public boolean isLoaded(UUID tenantId) {

        return userGroupRepository
                .count()>0;
    }
}