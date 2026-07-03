package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
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

    private static final Map<String, List<String>> GROUP_MENU_MAP = Map.ofEntries(

            // =========================
            // PLATFORM TEAM
            // =========================
            Map.entry("PLATFORM_TEAM", List.of("*")),

            // =========================
            // SECURITY TEAM
            // =========================
            Map.entry("SECURITY_TEAM", List.of(
                    "DASHBOARD",
                    "ACCESS_MGMT",
                    "USER_MGMT",
                    "USERS",
                    "GROUPS",
                    "ROLE_MGMT",
                    "ROLES",
                    "PERMISSIONS",
                    "MENU_MGMT",
                    "MENUS"
            )),

            // =========================
            // TENANT ADMIN
            // =========================
            Map.entry("TENANT_ADMIN_TEAM", List.of(
                    "DASHBOARD",
                    "ACCESS_MGMT",
                    "USER_MGMT",
                    "USERS",
                    "GROUPS"
            )),

            // =========================
            // OPERATIONS
            // =========================
            Map.entry("OPERATIONS_TEAM", List.of(
                    "DASHBOARD",
                    "FLIGHT_OPS",
                    "FLIGHT_SCHEDULES",
                    "CODESHARE_FLIGHTS",
                    "INGESTION",
                    "PROCESSING_JOBS",
                    "VALIDATION_ERRORS"
            )),

            // =========================
            // FLIGHT OPERATIONS
            // =========================
            Map.entry("FLIGHT_OPERATIONS_TEAM", List.of(
                    "DASHBOARD",
                    "FLIGHT_OPS",
                    "FLIGHT_SCHEDULES",
                    "CODESHARE_FLIGHTS"
            )),

            // =========================
            // BOOKING TEAM
            // =========================
            Map.entry("BOOKING_TEAM", List.of(
                    "DASHBOARD"
            )),

            // =========================
            // CUSTOMER SUPPORT
            // =========================
            Map.entry("CUSTOMER_SUPPORT_TEAM", List.of(
                    "DASHBOARD"
            )),

            // =========================
            // AUDIT TEAM
            // =========================
            Map.entry("AUDIT_TEAM", List.of(
                    "DASHBOARD",
                    "AUDIT"
            )),

            // =========================
            // ANALYTICS
            // =========================
            Map.entry("ANALYTICS_TEAM", List.of(
                    "DASHBOARD",
                    "REPORTS"
            )),

            // =========================
            // DEFAULT USERS
            // =========================
            Map.entry("DEFAULT_USERS", List.of(
                    "DASHBOARD"
            ))
    );


    @Transactional
    public void load(UUID tenantId) {

        log.info("⏳ GroupMenuLoader: creating group-menu mappings...");

        assignTenantMenus(tenantId);

        log.info("✅ GroupMenuLoader: {} mappings created.", tenantId);
    }

    private void assignTenantMenus(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Group> groups =
                groupRepository.findByTenant(tenant);

        List<Menu> menus =
                menuRepository.findByTenant(tenant);

        if (groups.isEmpty() || menus.isEmpty()) {

            log.warn(
                    "⚠ Skipping tenant {} — groups or menus missing",
                    tenant.getTenantCode()
            );

            return;
        }

        Set<String> existingMappings =
                groupMenuRepository.findMappings(tenant);

        List<GroupMenu> toSave =
                new ArrayList<>();

        Map<String, Menu> menuByCode =
                new HashMap<>();

        for (Menu menu : menus) {
            menuByCode.put(menu.getCode(), menu);
        }

        for (Group group : groups) {

            List<String> allowedMenus =
                    GROUP_MENU_MAP.getOrDefault(
                            group.getCode(),
                            List.of()
                    );

            // PLATFORM TEAM => ALL MENUS
            if (allowedMenus.contains("*")) {

                for (Menu menu : menus) {

                    saveGroupMenu(
                            tenant,
                            group,
                            menu,
                            existingMappings,
                            toSave
                    );
                }

                continue;
            }

            for (String menuCode : allowedMenus) {

                Menu menu = menuByCode.get(menuCode);

                if (menu == null) {

                    log.warn(
                            "⚠ Menu [{}] not found for group [{}]",
                            menuCode,
                            group.getCode()
                    );

                    continue;
                }

                saveGroupMenu(
                        tenant,
                        group,
                        menu,
                        existingMappings,
                        toSave
                );
            }
        }

        if (!toSave.isEmpty()) {

            groupMenuRepository.saveAll(toSave);

            log.info(
                    "✅ Tenant [{}]: {} group-menu mappings created.",
                    tenant.getTenantCode(),
                    toSave.size()
            );

        } else {

            log.info(
                    "✅ Tenant [{}]: group-menu mappings already exist.",
                    tenant.getTenantCode()
            );
        }
    }
    private void saveGroupMenu(
            Tenant tenant,
            Group group,
            Menu menu,
            Set<String> existingMappings,
            List<GroupMenu> toSave
    ) {

        String key =
                group.getCode() + ":" + menu.getCode();

        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);

        toSave.add(
                GroupMenu.builder()
                        .tenant(tenant)
                        .group(group)
                        .menu(menu)
                        .build()
        );
    }

    // ===============================
    // 🔐 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long actual = groupMenuRepository.countByTenantId(tenantId);

        return actual >= 0;
    }
}
