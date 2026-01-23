package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.model.entities.Group;
import com.codeshare.airline.auth.model.entities.Tenant;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class GroupLoader {

    private final GroupRepository groupRepository;
    private final TenantRepository tenantRepository;

    private static final List<String> BASE_GROUPS = List.of(
            "ADMIN",
            "IT",
            "OPS"
    );

    public void load(List<UUID> tenantIds) {

        log.info("⏳ GroupLoader: ensuring base groups for {} tenants...", tenantIds.size());

        List<Group> groupsToSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            for (String code : BASE_GROUPS) {

                boolean exists =
                        groupRepository.existsByTenantAndCode(tenant, code);

                if (exists) {
                    continue;
                }

                groupsToSave.add(
                        Group.builder()
                                .tenant(tenant)
                                .code(code)
                                .name(code + " Group")
                                .description(code + " Group for tenant " + tenant.getTenantCode())
                                .build()
                );
            }
        }

        if (!groupsToSave.isEmpty()) {
            groupRepository.saveAll(groupsToSave);
            log.info("✔ GroupLoader: {} groups created.", groupsToSave.size());
        } else {
            log.info("✔ GroupLoader: all groups already exist.");
        }
    }

    /**
     * Tenant-aware initialization check
     */
    public boolean isLoaded() {
        return groupRepository.count()>0;
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            log.warn("⚠ Invalid tenant UUID '{}'", id);
            return null;
        }
    }
}


