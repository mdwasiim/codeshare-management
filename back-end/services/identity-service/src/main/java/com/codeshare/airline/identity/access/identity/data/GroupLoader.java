package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.GroupSeed;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupLoader {

    private final GroupRepository groupRepository;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("GroupLoader: ensuring groups for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingCodes = groupRepository.findCodesByTenant(tenant);
        List<Group> groupsToSave = new ArrayList<>();

        for (GroupSeed seed : bootstrapData.groups()) {
            if (existingCodes.contains(seed.code())) {
                continue;
            }

            groupsToSave.add(Group.builder()
                    .tenant(tenant)
                    .code(seed.code())
                    .name(seed.name())
                    .description(seed.description())
                    .build());
        }

        if (!groupsToSave.isEmpty()) {
            groupRepository.saveAll(groupsToSave);
            log.info("GroupLoader: {} groups created.", groupsToSave.size());
        } else {
            log.info("GroupLoader: all groups already exist.");
        }
    }

    public boolean isLoaded(UUID tenantId) {
        long expected = bootstrapData.groups().size();
        long actual = groupRepository.countByTenantId(tenantId);
        return actual >= expected;
    }
}
