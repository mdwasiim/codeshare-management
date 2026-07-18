package com.codeshare.airline.identity.access.authorization.service.serviceImpl;

import com.codeshare.airline.platform.core.dto.tenant.MenuBackupDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;
import com.codeshare.airline.platform.core.enums.tenant.MenuNavigationType;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.service.core.UserContextService;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.authorization.mappers.MenuMapper;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.authorization.service.MenuService;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private static final Comparator<Menu> MENU_ORDER =
            Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Menu::getCode, Comparator.nullsLast(String::compareTo));

    private final UserContextService userContextService;
    private final MenuRepository repository;
    private final GroupMenuRepository groupMenuRepository;
    private final GroupRepository groupRepository;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;
    private final MenuMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public MenuDTO create(MenuDTO dto) {
        TenantContext ctx = TenantContextHolder.getTenant();

        Menu entity = mapper.toEntity(dto);
        entity.setCode(dto.getLabel().trim().toUpperCase().replaceAll("\\s+", "_"));
        entity.setTenantId(ctx.getId());

        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        }

        normalizePlacementLabels(entity);

        Menu saved = repository.save(entity);
        replaceMenuGroups(saved, dto.getGroupIds(), ctx.getId());

        return toDtoWithGroups(saved);
    }

    @Override
    public MenuDTO update(Long id, MenuDTO dto) {
        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        mapper.updateEntityFromDto(dto, entity);

        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        } else {
            entity.setParentMenu(null);
        }

        normalizePlacementLabels(entity);

        Menu saved = repository.save(entity);
        replaceMenuGroups(saved, dto.getGroupIds(), saved.getTenantId());

        return toDtoWithGroups(saved);
    }

    private void normalizePlacementLabels(Menu entity) {
        entity.setTopbarLabel(trimToNull(entity.getTopbarLabel()));
        entity.setSidebarLabel(trimToNull(entity.getSidebarLabel()));

        if (entity.getParentMenu() == null) {
            entity.setSidebarLabel(null);
        } else {
            entity.setTopbarLabel(null);
        }

        entity.setNavigationType(resolveNavigationType(entity));
        if (entity.getNavigationType() == MenuNavigationType.SECTION) {
            entity.setFrontendPath(null);
            entity.setExternalUrl(null);
        } else if (entity.getNavigationType() == MenuNavigationType.INTERNAL_LINK) {
            entity.setExternalUrl(null);
        } else {
            entity.setFrontendPath(null);
        }
    }

    private MenuNavigationType resolveNavigationType(Menu entity) {
        if (entity.getNavigationType() != null) {
            return entity.getNavigationType();
        }

        if (entity.getExternalUrl() != null && !entity.getExternalUrl().isBlank()) {
            return MenuNavigationType.EXTERNAL_LINK;
        }

        return entity.getFrontendPath() == null || entity.getFrontendPath().isBlank()
                ? MenuNavigationType.SECTION
                : MenuNavigationType.INTERNAL_LINK;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDtoWithGroups)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getRootMenus() {
        return mapper.toDTOList(
                repository.findByTenantIdAndParentMenuIsNullOrderByDisplayOrderAscCodeAsc(TenantContextHolder.getTenant().getId())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllForManagement() {
        TenantContext ctx = TenantContextHolder.getTenant();

        return repository.findByTenantIdOrderByDisplayOrderAscCodeAsc(ctx.getId())
                .stream()
                .sorted(MENU_ORDER)
                .map(this::toDtoWithGroups)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllByTenant() {
        TenantContext ctx = TenantContextHolder.getTenant();
        User user = userContextService.getCurrentUser();

        List<Group> groups = user.getUserGroups()
                .stream()
                .map(UserGroup::getGroup)
                .toList();

        Set<String> permissionCodes = rolePermissionAssignmentService.resolvePermissionCodes(user.getId())
                .stream()
                .filter(Objects::nonNull)
                .map(this::normalizePermission)
                .collect(Collectors.toSet());

        List<Menu> allowedMenus = groupMenuRepository.findMenusByGroupsAndTenant(groups, ctx.getId())
                .stream()
                .filter(this::isVisible)
                .filter(menu -> hasPermission(permissionCodes, menu.getPermissionCode()))
                .toList();

        Set<Menu> allowedSet = new HashSet<>(allowedMenus);
        for (Menu menu : allowedMenus) {
            Menu parent = menu.getParentMenu();
            while (parent != null) {
                if (isVisible(parent)) {
                    allowedSet.add(parent);
                }
                parent = parent.getParentMenu();
            }
        }

        Set<Menu> prunedMenus = allowedSet.stream()
                .filter(this::isVisible)
                .filter(menu -> isNavigable(menu) || hasVisibleDescendant(menu, allowedSet))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return prunedMenus.stream()
                .sorted(MENU_ORDER)
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Menu menu = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        repository.delete(menu);
    }

    @Override
    public List<MenuBackupDTO> getAllMenuBackup() {
        try {
            List<MenuBackupDTO> menus = repository.findAll()
                    .stream()
                    .map(menu -> {
                        MenuBackupDTO dto = new MenuBackupDTO();
                        dto.setCode(menu.getCode());
                        dto.setParentCode(menu.getParentMenu() != null ? menu.getParentMenu().getCode() : null);
                        dto.setLabel(menu.getLabel());
                        dto.setTopbarLabel(menu.getTopbarLabel());
                        dto.setSidebarLabel(menu.getSidebarLabel());
                        dto.setIcon(menu.getIcon());
                        dto.setNavigationType(menu.getNavigationType());
                        dto.setFrontendPath(menu.getFrontendPath());
                        dto.setExternalUrl(menu.getExternalUrl());
                        dto.setPermissionCode(menu.getPermissionCode());
                        dto.setDisplayOrder(menu.getDisplayOrder());
                        dto.setVisible(menu.getVisible());
                        return dto;
                    })
                    .toList();

            String backupDir = "backup/menu-backup";
            Path backupPath = Paths.get(backupDir);
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }

            String fileName = "menu-backup-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                    ".json";

            File backupFile = backupPath.resolve(fileName).toFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(backupFile, menus);

            log.info("Menu backup created: {}", backupFile.getAbsolutePath());
            return menus;
        } catch (Exception e) {
            log.error("Failed to create menu backup", e);
            throw new RuntimeException("Menu backup failed", e);
        }
    }

    private void replaceMenuGroups(Menu menu, List<Long> groupIds, Long tenantId) {
        groupMenuRepository.deleteByMenu_Id(menu.getId());
        groupMenuRepository.flush();

        if (groupIds == null || groupIds.isEmpty()) {
            return;
        }

        List<GroupMenu> mappings = groupIds.stream()
                .distinct()
                .map(groupId -> {
                    Group group = groupRepository.findById(groupId)
                            .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));

                    if (!Objects.equals(group.getTenantId(), tenantId)) {
                        throw new RuntimeException("Group does not belong to current tenant: " + groupId);
                    }

                    return (GroupMenu) GroupMenu.builder()
                            .tenantId(tenantId)
                            .group(group)
                            .menu(menu)
                            .build();
                })
                .toList();

        groupMenuRepository.saveAll(mappings);
    }

    private MenuDTO toDtoWithGroups(Menu menu) {
        MenuDTO dto = mapper.toDTO(menu);
        dto.setGroupIds(
                groupMenuRepository.findByMenu_Id(menu.getId())
                        .stream()
                        .map(groupMenu -> groupMenu.getGroup().getId())
                        .toList()
        );
        return dto;
    }

    private boolean hasVisibleDescendant(Menu candidate, Set<Menu> allowedMenus) {
        return allowedMenus.stream().anyMatch(menu -> isDescendantOf(menu, candidate));
    }

    private boolean isDescendantOf(Menu menu, Menu ancestor) {
        Menu parent = menu.getParentMenu();
        while (parent != null) {
            if (Objects.equals(parent.getId(), ancestor.getId())) {
                return true;
            }
            parent = parent.getParentMenu();
        }
        return false;
    }

    private boolean isVisible(Menu menu) {
        return menu != null && !Boolean.FALSE.equals(menu.getVisible());
    }

    private boolean isNavigable(Menu menu) {
        return menu != null
                && menu.getNavigationType() == MenuNavigationType.INTERNAL_LINK
                && menu.getFrontendPath() != null
                && !menu.getFrontendPath().isBlank();
    }

    private boolean hasPermission(Set<String> permissionCodes, String requiredPermission) {
        if (requiredPermission == null || requiredPermission.isBlank()) {
            return true;
        }

        String normalized = normalizePermission(requiredPermission);
        if (permissionCodes.contains("*") || permissionCodes.contains(normalized)) {
            return true;
        }

        int idx = normalized.indexOf(':');
        if (idx < 0) {
            return false;
        }

        return permissionCodes.contains(normalized.substring(0, idx) + ":*");
    }

    private String normalizePermission(String permission) {
        return permission == null ? "" : permission.trim().toUpperCase(Locale.ROOT);
    }
}
