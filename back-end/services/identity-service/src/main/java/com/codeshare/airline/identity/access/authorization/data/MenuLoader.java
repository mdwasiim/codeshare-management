package com.codeshare.airline.identity.access.authorization.data;

import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;
    private final HostAirlineTenantClient tenantClient;

    private List<MenuDTO> loadMenuDefinitions() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:bootstrap/menus/*.json");

            if (resources.length > 0) {
                List<MenuDTO> definitions = new ArrayList<>();

                Arrays.stream(resources)
                        .sorted(Comparator.comparing(Resource::getFilename, Comparator.nullsLast(String::compareTo)))
                        .forEach(resource -> definitions.addAll(readMenuResource(resource)));

                return definitions;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load split menu definitions", e);
        }

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("bootstrap/menus.json")) {

            if (is == null) {
                throw new IllegalStateException("menus.json not found");
            }
            return readMenuDefinitions(is);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to load menu definitions",
                    e
            );
        }
    }

    private List<MenuDTO> readMenuResource(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            return readMenuDefinitions(is);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load menu definitions from " + resource.getDescription(),
                    e
            );
        }
    }

    private List<MenuDTO> readMenuDefinitions(InputStream is) throws java.io.IOException {
        JsonNode root = objectMapper.readTree(is);

        if (root == null || root.isNull()) {
            return List.of();
        }

        if (root.isArray()) {
            return objectMapper.convertValue(
                    root,
                    new TypeReference<List<MenuDTO>>() {}
            );
        }

        if (root.isObject()) {
            return List.of(objectMapper.convertValue(root, MenuDTO.class));
        }

        throw new IllegalStateException("Menu definitions must be a JSON array or object");
    }

    public void load(Long tenantId) {

        log.info("MenuLoader: ensuring menus for tenant {}", tenantId);

        String tenantCode = resolveTenantCode(tenantId);
        Set<String> existingCodes = menuRepository.findCodesByTenantId(tenantId);
        List<MenuDTO> menuDefinitions = loadMenuDefinitions();
        Set<String> definitionCodes = menuDefinitions.stream()
                .map(MenuDTO::getCode)
                .collect(Collectors.toSet());

        List<Menu> toSave = new ArrayList<>();
        List<Menu> toUpdate = new ArrayList<>();
        Map<String, Menu> codeMap = new HashMap<>();
        Map<String, Menu> allMenus = menuRepository.findByTenantId(tenantId)
                .stream()
                .collect(Collectors.toMap(Menu::getCode, m -> m));

        for (MenuDTO dto : menuDefinitions) {

            if (existingCodes.contains(dto.getCode())) {
                Menu existing = allMenus.get(dto.getCode());
                if (existing != null && updateMenuDefinition(existing, dto)) {
                    toUpdate.add(existing);
                }
                continue;
            }

            Menu menu = Menu.builder()
                    .code(dto.getCode())
                    .label(dto.getLabel())
                    .topbarLabel(dto.getTopbarLabel())
                    .sidebarLabel(dto.getSidebarLabel())
                    .icon(dto.getIcon())
                    .route(dto.getRoute())
                    .permission(dto.getPermission())
                    .displayOrder(dto.getDisplayOrder())
                    .visible(dto.getVisible() == null ? Boolean.TRUE : dto.getVisible())
                    .tenantId(tenantId)
                    .build();

            toSave.add(menu);
            codeMap.put(dto.getCode(), menu);
        }

        allMenus.putAll(codeMap);

        for (MenuDTO dto : menuDefinitions) {
            Menu child = allMenus.get(dto.getCode());
            if (child == null) {
                continue;
            }

            Menu parent = dto.getParentCode() == null ? null : allMenus.get(dto.getParentCode());

            if (!Objects.equals(child.getParentMenu(), parent)) {
                child.setParentMenu(parent);
                if (!toSave.contains(child) && !toUpdate.contains(child)) {
                    toUpdate.add(child);
                }
            }
        }

        for (Menu existing : allMenus.values()) {
            if (definitionCodes.contains(existing.getCode()) || !isBootstrapManagedMenu(existing.getCode())) {
                continue;
            }

            if (!Boolean.FALSE.equals(existing.getVisible())) {
                existing.setVisible(Boolean.FALSE);
                if (!toUpdate.contains(existing)) {
                    toUpdate.add(existing);
                }
            }
        }

        if (!toSave.isEmpty() || !toUpdate.isEmpty()) {
            List<Menu> changes = new ArrayList<>();
            changes.addAll(toSave);
            changes.addAll(toUpdate);

            menuRepository.saveAll(changes);
            log.info("MenuLoader: {} menus created and {} menus updated for {}", toSave.size(), toUpdate.size(), tenantCode);
        }
    }

    private boolean updateMenuDefinition(Menu menu, MenuDTO dto) {
        boolean changed = false;

        if (!Objects.equals(menu.getLabel(), dto.getLabel())) {
            menu.setLabel(dto.getLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getTopbarLabel(), dto.getTopbarLabel())) {
            menu.setTopbarLabel(dto.getTopbarLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getSidebarLabel(), dto.getSidebarLabel())) {
            menu.setSidebarLabel(dto.getSidebarLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getIcon(), dto.getIcon())) {
            menu.setIcon(dto.getIcon());
            changed = true;
        }
        if (!Objects.equals(menu.getRoute(), dto.getRoute())) {
            menu.setRoute(dto.getRoute());
            changed = true;
        }
        if (!Objects.equals(menu.getPermission(), dto.getPermission())) {
            menu.setPermission(dto.getPermission());
            changed = true;
        }
        if (!Objects.equals(menu.getDisplayOrder(), dto.getDisplayOrder())) {
            menu.setDisplayOrder(dto.getDisplayOrder());
            changed = true;
        }
        Boolean visible = dto.getVisible() == null ? Boolean.TRUE : dto.getVisible();
        if (!Objects.equals(menu.getVisible(), visible)) {
            menu.setVisible(visible);
            changed = true;
        }

        return changed;
    }

    private boolean isBootstrapManagedMenu(String code) {
        return code != null && (
                code.startsWith("M_")
                        || Set.of(
                        "ADMINISTRATION",
                        "INGESTION",
                        "DASHBOARD",
                        "DASHBOARD_OVERVIEW",
                        "DASHBOARD_PRODUCT",
                        "TENANT_ONBOARDING",
                        "TENANT_SETUP",
                        "TENANTS",
                        "TENANT_IDENTITY",
                        "TENANT_IDENTITY_PROVIDERS",
                        "TENANT_OIDC_CONFIG",
                        "TENANT_INGESTION",
                        "TENANT_INGESTION_PROFILES",
                        "TENANT_INGESTION_CHANNELS",
                        "TENANT_PARTNER_SETUP",
                        "TENANT_PARTNERS",
                        "TENANT_PARTNER_PROFILES",
                        "TENANT_COMMUNICATION_PROFILES",
                        "TENANT_DISTRIBUTION_PROFILES",
                        "ACCESS_MGMT",
                        "IDENTITY_MGMT",
                        "AUTHORIZATION",
                        "ACCESS_ASSIGNMENT",
                        "USERS",
                        "GROUPS",
                        "ROLES",
                        "PERMISSIONS",
                        "MENUS",
                        "GROUP_ROLES",
                        "ROLE_PERMISSIONS",
                        "USER_GROUPS",
                        "GROUP_MENUS",
                        "SYSTEM_CONFIG",
                        "SSIM_INBOUND",
                        "SCHEDULE_MANAGER",
                        "LIVE_SCHEDULE",
                        "SCENARIO_MANGER",
                        "INBOUND_SCHEDULES",
                        "INBOUND_ASM/SSM",
                        "CODESHARE_SCHEDULES",
                        "OPERATING_SCHEDULES",
                        "LIVE_SCHEDULES",
                        "SCHEDULE_UPDATE",
                        "MASTERS",
                        "M_GEOGRAPHY",
                        "M_AIRLINES",
                        "M_AIRCRAFT",
                        "M_FLIGHT",
                        "M_SCHEDULE",
                        "M_MESSAGING",
                        "M_TERMINAL"
                ).contains(code)
        );
    }

    public boolean isLoaded(Long tenantId) {

        long actual = menuRepository.countByTenantId(tenantId);
        long expected = loadMenuDefinitions().size();

        return actual >= expected;
    }

    private String resolveTenantCode(Long tenantId) {
        return tenantClient.getAll().stream()
                .filter(tenant -> tenantId.equals(tenant.getId()))
                .map(tenant -> tenant.getTenantCode())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tenant not found in tenant-service: " + tenantId));
    }
}
