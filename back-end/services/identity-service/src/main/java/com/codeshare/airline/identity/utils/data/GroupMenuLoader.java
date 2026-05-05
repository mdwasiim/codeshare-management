package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.*;
import com.codeshare.airline.identity.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMenuLoader {

    private final GroupMenuRepository groupMenuRepository;
    private final GroupRepository groupRepository;
    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public void load(List<UUID> tenantIds) {

        log.info("⏳ GroupMenuLoader: creating group-menu mappings...");

        int total = 0;

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            total += assignTenantMenus(tenantId);
        }

        log.info("✅ GroupMenuLoader: {} mappings created.", total);
    }

    private int assignTenantMenus(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Group> groups = groupRepository.findByTenant(tenant);
        List<Menu> menus = menuRepository.findByTenant(tenant);

        if (groups.isEmpty() || menus.isEmpty()) {
            log.warn("⚠ Skipping tenant {} — groups or menus missing", tenant.getTenantCode());
            return 0;
        }

        // 🔥 Load existing mappings once
        Set<String> existingMappings = groupMenuRepository.findMappings(tenant);

        List<GroupMenu> toSave = new ArrayList<>();

        for (Group group : groups) {

            for (Menu menu : menus) {

                String key = group.getCode() + ":" + menu.getCode();

                if (existingMappings.contains(key)) continue;

                toSave.add(
                        GroupMenu.builder()
                                .tenant(tenant)
                                .group(group)
                                .menu(menu)
                                .build()
                );
            }
        }

        if (!toSave.isEmpty()) {
            groupMenuRepository.saveAll(toSave);
        }

        log.info("Tenant {}: {} group-menu mappings created.",
                tenant.getTenantCode(), toSave.size());

        return toSave.size();
    }

    // ===============================
    // 🔐 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long groupCount = groupRepository.countByTenantId(tenantId);
        long menuCount = menuRepository.countByTenantId(tenantId);

        long expected = groupCount * menuCount;
        long actual = groupMenuRepository.countByTenantId(tenantId);

        return actual >= expected;
    }
}