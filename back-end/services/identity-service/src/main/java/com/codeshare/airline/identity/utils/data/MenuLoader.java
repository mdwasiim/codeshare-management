package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.MenuRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    // Preload JSON once
    private static final String MENU_JSON ="[{\"label\":\"Home\",\"path\":\"/home\",\"displayOrder\":1,\"items\":[{\"label\":\"Dashboard\",\"icon\":\"pi pi-fw pi-home\",\"displayOrder\":1,\"routerLink\":[\"/dashboard\"]},{\"label\":\"Product\",\"icon\":\"pi pi-fw pi-box\",\"displayOrder\":3,\"routerLink\":[\"/settings/products\"]}]},{\"label\":\"Flight Operations\",\"icon\":\"pi pi-fw pi-send\",\"path\":\"/flights-root\",\"displayOrder\":2,\"items\":[{\"label\":\"Flight Schedules\",\"icon\":\"pi pi-fw pi-calendar\",\"displayOrder\":1,\"routerLink\":[\"/flights\"]},{\"label\":\"Codeshare Flights\",\"icon\":\"pi pi-fw pi-share-alt\",\"displayOrder\":2,\"routerLink\":[\"/codeshare\"]}]},{\"label\":\"Schedule Ingestion\",\"icon\":\"pi pi-fw pi-upload\",\"path\":\"/ingestion\",\"displayOrder\":3,\"items\":[{\"label\":\"Upload Schedule\",\"icon\":\"pi pi-fw pi-upload\",\"displayOrder\":1,\"routerLink\":[\"/ingestion/upload\"]},{\"label\":\"Processing Jobs\",\"icon\":\"pi pi-fw pi-cog\",\"displayOrder\":2,\"routerLink\":[\"/ingestion/jobs\"]},{\"label\":\"Validation Errors\",\"icon\":\"pi pi-fw pi-exclamation-triangle\",\"displayOrder\":3,\"routerLink\":[\"/ingestion/errors\"]}]},{\"label\":\"Partner Management\",\"icon\":\"pi pi-fw pi-users\",\"path\":\"/partners-root\",\"displayOrder\":4,\"items\":[{\"label\":\"Airline Partners\",\"icon\":\"pi pi-fw pi-building\",\"displayOrder\":1,\"routerLink\":[\"/partners\"]},{\"label\":\"Agreements\",\"icon\":\"pi pi-fw pi-file\",\"displayOrder\":2,\"routerLink\":[\"/agreements\"]}]},{\"label\":\"Reference Data\",\"icon\":\"pi pi-fw pi-database\",\"path\":\"/reference\",\"displayOrder\":5,\"items\":[{\"label\":\"Airports\",\"icon\":\"pi pi-fw pi-map\",\"displayOrder\":1,\"routerLink\":[\"/reference/airports\"]},{\"label\":\"Aircraft Types\",\"icon\":\"pi pi-fw pi-car\",\"displayOrder\":2,\"routerLink\":[\"/reference/aircraft\"]},{\"label\":\"Routes\",\"icon\":\"pi pi-fw pi-directions\",\"displayOrder\":3,\"routerLink\":[\"/reference/routes\"]}]},{\"label\":\"System Configuration\",\"icon\":\"pi pi-fw pi-cog\",\"path\":\"/settings/organizations\",\"displayOrder\":6,\"items\":[{\"label\":\"Organization\",\"icon\":\"pi pi-fw pi-globe\",\"path\":\"/settings/organizations\",\"displayOrder\":1,\"items\":[{\"label\":\"All Organizations\",\"icon\":\"pi pi-fw pi-list\",\"displayOrder\":1,\"routerLink\":[\"/settings/organizations\"]},{\"label\":\"Create Organization\",\"icon\":\"pi pi-fw pi-plus-circle\",\"displayOrder\":2,\"routerLink\":[\"/settings/organizations/create\"]}]}]},{\"label\":\"Access Management\",\"icon\":\"pi pi-fw pi-shield\",\"displayOrder\":7,\"path\":\"/iam\",\"items\":[{\"label\":\"User Management\",\"icon\":\"pi pi-fw pi-users\",\"path\":\"/iam/users\",\"displayOrder\":1,\"items\":[{\"label\":\"Users\",\"icon\":\"pi pi-fw pi-user\",\"displayOrder\":1,\"routerLink\":[\"/iam/users\"]},{\"label\":\"Groups\",\"icon\":\"pi pi-fw pi-users\",\"displayOrder\":2,\"routerLink\":[\"/iam/groups\"]}]},{\"label\":\"Role Management\",\"icon\":\"pi pi-fw pi-id-card\",\"path\":\"/iam/roles\",\"displayOrder\":2,\"items\":[{\"label\":\"Roles\",\"icon\":\"pi pi-fw pi-briefcase\",\"displayOrder\":1,\"routerLink\":[\"/iam/roles\"]},{\"label\":\"Permissions\",\"icon\":\"pi pi-fw pi-lock\",\"displayOrder\":2,\"routerLink\":[\"/iam/permissions\"]}]},{\"label\":\"Menu Management\",\"icon\":\"pi pi-fw pi-bars\",\"path\":\"/iam/menus\",\"displayOrder\":3,\"items\":[{\"label\":\"Menus\",\"icon\":\"pi pi-fw pi-list\",\"displayOrder\":1,\"routerLink\":[\"/iam/menus\"]}]}]}]";

    public void load(List<UUID> tenantIds) {

        log.info("⏳ MenuLoader: ensuring menus for {} tenants...", tenantIds.size());

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            if (menuRepository.existsByTenant(tenant)) {
                log.info(" Menus already exist for ingestion {}", tenant.getTenantCode());
                continue;
            }

            List<Menu> menus = buildMenusForTenant(tenant);
            menuRepository.saveAll(menus);

            log.info(" MenuLoader: {} menus created for ingestion {}", menus.size(), tenant.getTenantCode());
        }
    }

    public boolean isLoaded() {
        return menuRepository.count()>0;
    }

    private List<Menu> buildMenusForTenant(Tenant tenant) {

        List<MenuDTO> rootMenus;
        try {
            rootMenus = objectMapper.readValue(MENU_JSON, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse menu JSON", e);
        }

        List<Menu> collector = new ArrayList<>();
        rootMenus.sort(Comparator.comparing(MenuDTO::getDisplayOrder));
        for (MenuDTO dto : rootMenus) {
            processMenu(dto, null, tenant, collector);
        }

        return collector;
    }

    private void processMenu(
            MenuDTO dto,
            Menu parent,
            Tenant tenant,
            List<Menu> collector
    ) {
        String path = generatePath(dto, parent);

        Menu menu = Menu.builder()
                .label(dto.getLabel())
                .icon(dto.getIcon())
                .path(path) // 🔥 use generated path
                .routerLink(dto.getRouterLink())
                .displayOrder(dto.getDisplayOrder())
                .tenant(tenant)
                .parentMenu(parent)
                .build();

        collector.add(menu);

        if (dto.getItems() != null) {
            dto.getItems().sort(Comparator.comparing(MenuDTO::getDisplayOrder));
            for (MenuDTO child : dto.getItems()) {
                processMenu(child, menu, tenant, collector);
            }
        }
    }

    private String generatePath(MenuDTO dto, Menu parent) {

        String slug = dto.getLabel()
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-");

        String current = "/" + slug;

        if (parent == null || parent.getPath() == null) {
            return current;
        }

        return parent.getPath() + current;
    }
}
