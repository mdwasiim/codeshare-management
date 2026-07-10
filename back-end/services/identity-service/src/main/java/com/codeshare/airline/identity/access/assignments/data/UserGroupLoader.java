package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final IdentityBootstrapData bootstrapData;
    private final HostAirlineTenantClient tenantClient;

    @Transactional
    public void load(UUID tenantId) {
        log.info("UserGroupLoader: assigning users to groups for tenant {}", tenantId);
        assignUserGroups(tenantId);
    }

    private void assignUserGroups(UUID tenantId) {
        List<User> users = userRepository.findByTenantId(tenantId);
        List<Group> groups = groupRepository.findByTenantId(tenantId);
        String tenantCode = resolveTenantCode(tenantId);

        if (users.isEmpty() || groups.isEmpty()) {
            log.warn("Skipping tenant [{}] because users or groups are missing", tenantCode);
            return;
        }

        Set<String> existingMappings = userGroupRepository.findMappingsByTenantId(tenantId);
        List<UserGroup> toSave = new ArrayList<>();

        Map<String, User> userByUsername = new HashMap<>();
        users.forEach(user -> userByUsername.put(user.getUsername(), user));

        Map<String, Group> groupByCode = new HashMap<>();
        groups.forEach(group -> groupByCode.put(group.getCode(), group));

        bootstrapData.userGroups().forEach((username, groupCodes) -> {
            User user = userByUsername.get(username);
            if (user == null) {
                log.warn("User [{}] not found for user-group bootstrap", username);
                return;
            }

            for (String groupCode : groupCodes) {
                Group group = groupByCode.get(groupCode);
                if (group == null) {
                    log.warn("Group [{}] not found for user [{}]", groupCode, username);
                    continue;
                }

                saveUserGroup(tenantId, user, group, existingMappings, toSave);
            }
        });

        if (!toSave.isEmpty()) {
            userGroupRepository.saveAll(toSave);
            log.info("Tenant [{}]: {} user-group mappings created.", tenantCode, toSave.size());
        } else {
            log.info("Tenant [{}]: user-group mappings already exist.", tenantCode);
        }
    }

    private void saveUserGroup(UUID tenantId,
                               User user,
                               Group group,
                               Set<String> existingMappings,
                               List<UserGroup> toSave) {
        String key = user.getId() + ":" + group.getId();
        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);
        toSave.add(UserGroup.builder()
                .tenantId(tenantId)
                .user(user)
                .group(group)
                .build());
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = userGroupRepository.countByTenantId(tenantId);
        long expected = bootstrapData.userGroups().values().stream()
                .mapToLong(List::size)
                .sum();
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
