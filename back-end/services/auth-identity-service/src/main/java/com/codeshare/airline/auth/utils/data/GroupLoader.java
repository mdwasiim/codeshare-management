package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.repository.GroupRepository;
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

    private final GroupRepository repo;

    private static final List<String> BASE_GROUPS = List.of(
            "ADMIN",
            "IT",
            "OPS"
    );

    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ GroupLoader: groups already present — skipping load.");
            return;
        }

        log.info("⏳ GroupLoader: creating default groups for {} tenants...", tenantIds.size());

        List<Group> groups = new ArrayList<>();

        for (String tenantIdString : tenantIds) {

            UUID tenantId;
            try {
                tenantId = UUID.fromString(tenantIdString);
            } catch (Exception ex) {
                log.warn("⚠ Invalid tenant UUID '{}', skipping...", tenantIdString);
                continue;
            }

            groups.add(createGroup(tenantId, "ADMIN", "Admin Group for tenant " + tenantId));
            groups.add(createGroup(tenantId, "IT", "IT Group for tenant " + tenantId));
            groups.add(createGroup(tenantId, "OPS", "Operations Group for tenant " + tenantId));
        }

        repo.saveAll(groups);

        log.info("✔ GroupLoader: {} groups inserted.", groups.size());
    }

    private Group createGroup(UUID tenantId, String code, String description) {
        return Group.builder()
                .tenantId(tenantId)
                .code(code)
                .name(description)
                .active(true)
                .build();
    }
}
