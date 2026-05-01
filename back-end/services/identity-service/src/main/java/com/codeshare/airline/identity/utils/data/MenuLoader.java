package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.MenuRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    // Preload JSON once
    private static final String MENU_JSON ="[{\"code\":\"HOME\",\"label\":\"Home\",\"displayOrder\":1},{\"code\":\"DASHBOARD\",\"label\":\"Dashboard\",\"icon\":\"pi pi-fw pi-home\",\"displayOrder\":1,\"parentCode\":\"HOME\"},{\"code\":\"DASHBOARD_OVERVIEW\",\"label\":\"Overview\",\"icon\":\"pi pi-fw pi-home\",\"route\":\"/dashboard\",\"displayOrder\":1,\"parentCode\":\"DASHBOARD\"},{\"code\":\"DASHBOARD_PRODUCT\",\"label\":\"Product\",\"icon\":\"pi pi-fw pi-box\",\"route\":\"/settings/products\",\"displayOrder\":3,\"parentCode\":\"DASHBOARD\"},{\"code\":\"FLIGHT_OPS\",\"label\":\"Flight Operations\",\"icon\":\"pi pi-fw pi-send\",\"displayOrder\":2},{\"code\":\"FLIGHT_SCHEDULES\",\"label\":\"Flight Schedules\",\"icon\":\"pi pi-fw pi-calendar\",\"route\":\"/flights\",\"displayOrder\":1,\"parentCode\":\"FLIGHT_OPS\"},{\"code\":\"CODESHARE_FLIGHTS\",\"label\":\"Codeshare Flights\",\"icon\":\"pi pi-fw pi-share-alt\",\"route\":\"/codeshare\",\"displayOrder\":2,\"parentCode\":\"FLIGHT_OPS\"},{\"code\":\"INGESTION\",\"label\":\"Schedule Ingestion\",\"icon\":\"pi pi-fw pi-upload\",\"displayOrder\":3},{\"code\":\"UPLOAD_SCHEDULE\",\"label\":\"Upload Schedule\",\"icon\":\"pi pi-fw pi-upload\",\"route\":\"/ingestion/upload\",\"displayOrder\":1,\"parentCode\":\"INGESTION\"},{\"code\":\"PROCESSING_JOBS\",\"label\":\"Processing Jobs\",\"icon\":\"pi pi-fw pi-cog\",\"route\":\"/ingestion/jobs\",\"displayOrder\":2,\"parentCode\":\"INGESTION\"},{\"code\":\"VALIDATION_ERRORS\",\"label\":\"Validation Errors\",\"icon\":\"pi pi-fw pi-exclamation-triangle\",\"route\":\"/ingestion/errors\",\"displayOrder\":3,\"parentCode\":\"INGESTION\"},{\"code\":\"PARTNER_MGMT\",\"label\":\"Partner Management\",\"icon\":\"pi pi-fw pi-users\",\"displayOrder\":4},{\"code\":\"AIRLINE_PARTNERS\",\"label\":\"Airline Partners\",\"icon\":\"pi pi-fw pi-building\",\"route\":\"/partners\",\"displayOrder\":1,\"parentCode\":\"PARTNER_MGMT\"},{\"code\":\"AGREEMENTS\",\"label\":\"Agreements\",\"icon\":\"pi pi-fw pi-file\",\"route\":\"/agreements\",\"displayOrder\":2,\"parentCode\":\"PARTNER_MGMT\"},{\"code\":\"REFERENCE_DATA\",\"label\":\"Reference Data\",\"icon\":\"pi pi-fw pi-database\",\"displayOrder\":5},{\"code\":\"AIRPORTS\",\"label\":\"Airports\",\"icon\":\"pi pi-fw pi-map\",\"route\":\"/reference/airports\",\"displayOrder\":1,\"parentCode\":\"REFERENCE_DATA\"},{\"code\":\"AIRCRAFT_TYPES\",\"label\":\"Aircraft Types\",\"icon\":\"pi pi-fw pi-car\",\"route\":\"/reference/aircraft\",\"displayOrder\":2,\"parentCode\":\"REFERENCE_DATA\"},{\"code\":\"ROUTES\",\"label\":\"Routes\",\"icon\":\"pi pi-fw pi-directions\",\"route\":\"/reference/routes\",\"displayOrder\":3,\"parentCode\":\"REFERENCE_DATA\"},{\"code\":\"SYSTEM_CONFIG\",\"label\":\"System Configuration\",\"icon\":\"pi pi-fw pi-cog\",\"displayOrder\":6},{\"code\":\"ORG_ROOT\",\"label\":\"Organization\",\"icon\":\"pi pi-fw pi-globe\",\"displayOrder\":1,\"parentCode\":\"SYSTEM_CONFIG\"},{\"code\":\"ORG_LIST\",\"label\":\"All Organizations\",\"icon\":\"pi pi-fw pi-list\",\"route\":\"/settings/organizations\",\"displayOrder\":1,\"parentCode\":\"ORG_ROOT\"},{\"code\":\"ORG_CREATE\",\"label\":\"Create Organization\",\"icon\":\"pi pi-fw pi-plus-circle\",\"route\":\"/settings/organizations/create\",\"displayOrder\":2,\"parentCode\":\"ORG_ROOT\"},{\"code\":\"ACCESS_MGMT\",\"label\":\"Access Management\",\"icon\":\"pi pi-fw pi-shield\",\"displayOrder\":7},{\"code\":\"USER_MGMT\",\"label\":\"User Management\",\"icon\":\"pi pi-fw pi-users\",\"displayOrder\":1,\"parentCode\":\"ACCESS_MGMT\"},{\"code\":\"USERS\",\"label\":\"Users\",\"icon\":\"pi pi-fw pi-user\",\"route\":\"/iam/users\",\"displayOrder\":1,\"parentCode\":\"USER_MGMT\"},{\"code\":\"GROUPS\",\"label\":\"Groups\",\"icon\":\"pi pi-fw pi-users\",\"route\":\"/iam/groups\",\"displayOrder\":2,\"parentCode\":\"USER_MGMT\"},{\"code\":\"ROLE_MGMT\",\"label\":\"Role Management\",\"icon\":\"pi pi-fw pi-id-card\",\"displayOrder\":2,\"parentCode\":\"ACCESS_MGMT\"},{\"code\":\"ROLES\",\"label\":\"Roles\",\"icon\":\"pi pi-fw pi-briefcase\",\"route\":\"/iam/roles\",\"displayOrder\":1,\"parentCode\":\"ROLE_MGMT\"},{\"code\":\"PERMISSIONS\",\"label\":\"Permissions\",\"icon\":\"pi pi-fw pi-lock\",\"route\":\"/iam/permissions\",\"displayOrder\":2,\"parentCode\":\"ROLE_MGMT\"},{\"code\":\"MENU_MGMT\",\"label\":\"Menu Management\",\"icon\":\"pi pi-fw pi-bars\",\"displayOrder\":3,\"parentCode\":\"ACCESS_MGMT\"},{\"code\":\"MENUS\",\"label\":\"Menus\",\"icon\":\"pi pi-fw pi-list\",\"route\":\"/iam/menus\",\"displayOrder\":1,\"parentCode\":\"MENU_MGMT\"}]";

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

        List<MenuDTO> flatMenus;

        try {
            flatMenus = objectMapper.readValue(MENU_JSON, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse menu JSON", e);
        }

        List<Menu> entities = new ArrayList<>();

        // 1. Create all menus
        for (MenuDTO dto : flatMenus) {
            Menu menu = Menu.builder()
                    .code(dto.getCode())
                    .label(dto.getLabel())
                    .icon(dto.getIcon())
                    .route(dto.getRoute())
                    .displayOrder(dto.getDisplayOrder())
                    .tenant(tenant)
                    .build();

            entities.add(menu);
        }

        // 2. Map code → entity
        Map<String, Menu> codeMap = new HashMap<>();
        for (int i = 0; i < flatMenus.size(); i++) {
            codeMap.put(flatMenus.get(i).getCode(), entities.get(i));
        }

        // 3. Assign parent
        for (int i = 0; i < flatMenus.size(); i++) {
            MenuDTO dto = flatMenus.get(i);

            if (dto.getParentCode() != null) {
                Menu parent = codeMap.get(dto.getParentCode());

                if (parent == null) {
                    throw new IllegalStateException("Parent not found: " + dto.getParentCode());
                }
                entities.get(i).setParentMenu(parent);
            }
        }

        return entities;
    }
}
