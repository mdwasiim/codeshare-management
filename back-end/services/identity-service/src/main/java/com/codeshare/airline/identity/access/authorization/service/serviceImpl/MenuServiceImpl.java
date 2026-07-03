package com.codeshare.airline.identity.access.authorization.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.MenuBackupDTO;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.service.core.UserContextService;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.access.authorization.service.MenuService;
import com.codeshare.airline.identity.access.identity.service.TenantService;
import com.codeshare.airline.identity.access.authorization.mappers.MenuMapper;
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
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final UserContextService userContextService;
    private final MenuRepository repository;
    private final GroupMenuRepository groupMenuRepository;
    private final GroupRepository groupRepository;

    private final MenuMapper mapper;

    private final TenantRepository tenantRepository;
    private final TenantService tenantService;

    private final ObjectMapper objectMapper;

    // ---------------------------------------------------------
    // CREATE NEW MENU FOR TENANT
    // ---------------------------------------------------------
    @Override
    public MenuDTO create(MenuDTO dto) {

        // 🔥 Get tenant from context (NOT from DTO)
        TenantContext ctx = TenantContextHolder.getTenant();

        // 🔥 Fetch tenant entity
        Tenant tenant = tenantRepository.findByTenantCode(ctx.getTenantCode())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // 🔥 Map DTO → Entity
        Menu entity = mapper.toEntity(dto);

        // 🔥 FIX: generate code
        entity.setCode(dto.getLabel().trim().toUpperCase().replaceAll("\\s+", "_"));
        // 🔥 Assign tenant
        entity.setTenant(tenant);

        // 🔥 Assign parent
        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        }

        normalizePlacementLabels(entity);

        Menu saved = repository.save(entity);
        replaceMenuGroups(saved, dto.getGroupIds(), tenant);

        return toDtoWithGroups(saved);
    }


    // ---------------------------------------------------------
    // UPDATE MENU DETAILS
    // ---------------------------------------------------------
    @Override
    public MenuDTO update(UUID id, MenuDTO dto) {

        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        // 🔥 Use mapper
        mapper.updateEntityFromDto(dto, entity);

        // 🔥 Handle parent manually
        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        } else {
            entity.setParentMenu(null);
        }

        normalizePlacementLabels(entity);

        Menu saved = repository.save(entity);
        replaceMenuGroups(saved, dto.getGroupIds(), saved.getTenant());

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
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }


    // ---------------------------------------------------------
    // GET MENU BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public MenuDTO getById(UUID id) {
        return repository.findById(id)
                .map(this::toDtoWithGroups)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }


    // ---------------------------------------------------------
    // GET TOP-LEVEL (ROOT) MENUS FOR TENANT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getRootMenus() {
        Tenant tenantByTenantCode = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());
        return mapper.toDTOList(repository.findByTenantIdAndParentMenuIsNull(tenantByTenantCode.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllForManagement() {
        TenantContext ctx = TenantContextHolder.getTenant();

        return repository.findByTenant_TenantCode(ctx.getTenantCode())
                .stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .map(this::toDtoWithGroups)
                .toList();
    }


    // ---------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllByTenant() {

        TenantContext ctx = TenantContextHolder.getTenant();
        User user = userContextService.getCurrentUser();

        List<Group> groups = user.getUserGroups()
                .stream()
                .map(UserGroup::getGroup)
                .toList();

        // 🔥 fetch allowed menus
        List<Menu> allowedMenus =
                groupMenuRepository.findMenusByGroupsAndTenant(groups, ctx.getTenantCode());
        /*List<Menu> allowedMenus =
                repository.findAll();*/
        // 🔥 include parents
        Set<Menu> allowedSet = new HashSet<>(allowedMenus);

        for (Menu menu : allowedMenus) {
            Menu parent = menu.getParentMenu();
            while (parent != null) {
                allowedSet.add(parent);
                parent = parent.getParentMenu();
            }
        }

        allowedSet.forEach(menu -> {
            if (menu.getParentMenu() != null) {
                menu.getParentMenu().getId();
            }
        });
        log.info("Allowed menus count={}", allowedMenus.size());

        allowedMenus.forEach(m ->
                log.info(
                        "MENU code={} visible={} route={} permission={}",
                        m.getCode(),
                        m.getVisible(),
                        m.getRoute(),
                        m.getPermission()
                )
        );

        // ✅ RETURN FLAT LIST
        return allowedSet.stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)))
                .map(mapper::toDTO)
                .toList();
    }

    // ---------------------------------------------------------
    // DELETE MENU BY ID
    // ---------------------------------------------------------
    @Override
    public void delete(UUID id) {

        Menu menu = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Menu not found: " + id)
                );

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
                        dto.setParentCode(
                                menu.getParentMenu() != null
                                        ? menu.getParentMenu().getCode()
                                        : null
                        );
                        dto.setLabel(menu.getLabel());
                        dto.setTopbarLabel(menu.getTopbarLabel());
                        dto.setSidebarLabel(menu.getSidebarLabel());
                        dto.setIcon(menu.getIcon());
                        dto.setRoute(menu.getRoute());
                        dto.setPermission(menu.getPermission());
                        dto.setDisplayOrder(menu.getDisplayOrder());
                        dto.setVisible(menu.getVisible());
                        return dto;
                    })
                    .toList();

            // backup folder
            String backupDir = "backup/menu-backup";

            Path backupPath = Paths.get(backupDir);

            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }

            // filename with timestamp
            String fileName = "menu-backup-" +
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                    + ".json";

            File backupFile = backupPath.resolve(fileName).toFile();

            // write json
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(backupFile, menus);

            log.info("Menu backup created: {}", backupFile.getAbsolutePath());

            return menus;

        } catch (Exception e) {
            log.error("Failed to create menu backup", e);
            throw new RuntimeException("Menu backup failed", e);
        }
    }

    private void replaceMenuGroups(Menu menu, List<UUID> groupIds, Tenant tenant) {
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

                    if (!Objects.equals(group.getTenant().getId(), tenant.getId())) {
                        throw new RuntimeException("Group does not belong to current tenant: " + groupId);
                    }

                    GroupMenu mapping = GroupMenu.builder()
                            .tenant(tenant)
                            .group(group)
                            .menu(menu)
                            .build();
                    return mapping;
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
}
