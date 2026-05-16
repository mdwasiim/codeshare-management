package com.codeshare.airline.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.MenuBackupDTO;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.authentication.domain.TenantContext;
import com.codeshare.airline.identity.authentication.domain.TenantContextHolder;
import com.codeshare.airline.identity.authentication.service.core.UserContextService;
import com.codeshare.airline.identity.entities.*;
import com.codeshare.airline.identity.repository.GroupMenuRepository;
import com.codeshare.airline.identity.repository.GroupRepository;
import com.codeshare.airline.identity.repository.MenuRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.identity.service.MenuService;
import com.codeshare.airline.identity.service.TenantService;
import com.codeshare.airline.identity.utils.mappers.MenuMapper;
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
        Menu menu = repository.save(entity);
        if (dto.getGroupIds() != null) {

            List<GroupMenu> mappings = new ArrayList<>();

            for (UUID groupId : dto.getGroupIds()) {

                Group group = groupRepository.findById(groupId)
                        .orElseThrow();

                mappings.add(
                        GroupMenu.builder()
                                .tenant(tenant)
                                .group(group)
                                .menu(menu)
                                .build()
                );
            }

            groupMenuRepository.saveAll(mappings);
        }
        return mapper.toDTO(menu);
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

        return mapper.toDTO(repository.save(entity));
    }


    // ---------------------------------------------------------
    // GET MENU BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public MenuDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }


    // ---------------------------------------------------------
    // GET TOP-LEVEL (ROOT) MENUS FOR TENANT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getRootMenus() {
        TenantContext tenant = TenantContextHolder.getTenant();
        Tenant tenantByTenantCode = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());
        return mapper.toDTOList(repository.findByTenantIdAndParentMenuIsNull(tenantByTenantCode.getId()));
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
}
