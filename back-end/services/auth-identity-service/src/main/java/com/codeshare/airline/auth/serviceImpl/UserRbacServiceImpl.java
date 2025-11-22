package com.codeshare.airline.auth.serviceImpl;


import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.model.UserRbacResponse;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.MenuRoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.RolePermissionResolverService;
import com.codeshare.airline.auth.service.UserRbacService;
import com.codeshare.airline.common.auth.model.MenuDTO;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeshare.airline.common.constant.RedisCacheConstant.USER_RBAC;
import static com.codeshare.airline.common.constant.RedisCacheConstant.USER_RBAC_KEY;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = USER_RBAC)   // ✅ Applied at class level
public class UserRbacServiceImpl implements UserRbacService {

    private final UserRepository userRepository;
    private final MenuRoleRepository menuRoleRepository;
    private final MenuRepository menuRepository;
    private final RolePermissionResolverService rolePermissionResolverService;

    @Autowired
    public UserRbacServiceImpl(UserRepository userRepository, MenuRoleRepository menuRoleRepository, MenuRepository menuRepository, RolePermissionResolverService rolePermissionResolverService) {
        this.userRepository = userRepository;
        this.menuRoleRepository = menuRoleRepository;
        this.menuRepository = menuRepository;
        this.rolePermissionResolverService = rolePermissionResolverService;
    }


    // ---------------------------------------------
    // 🚀 REDIS CACHING (Annotation-based)
    // ---------------------------------------------

    @Override
    @Cacheable(key = USER_RBAC_KEY)
    public UserRbacResponse resolveUserRbac(UUID userId) {
        return computeRbac(userId); // Spring caches the output automatically
    }

    @Override
    @CacheEvict(key = USER_RBAC_KEY)
    public void invalidateUserRbacCache(UUID userId) {
        // Nothing else needed — cache entry is removed automatically
    }

    // ---------------------------------------------
    // RBAC COMPUTATION (No caching logic here)
    // ---------------------------------------------

    private UserRbacResponse computeRbac(UUID userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        UUID tenantId = user.getTenantId();

        // 1) Collect Roles (direct + group)
        Set<RoleDTO> roleDTOs = rolePermissionResolverService.resolveRoleNames(userId, tenantId);

        // 2) Collect Permissions
        Set<PermissionDTO> permissionDTOs = rolePermissionResolverService.resolvePermissionsNames(userId, tenantId);

        // 3) Collect Menus allowed by roles
        Set<UUID> allowedMenuIds = new LinkedHashSet<>();
        Set<UUID> roleIds = roleDTOs.stream().map(RoleDTO::getId).collect(Collectors.toSet());

        for (UUID rid : roleIds) {
            menuRoleRepository.findByRoleId(rid)
                    .forEach(mr -> allowedMenuIds.add(mr.getMenu().getId()));
        }

        List<MenuDTO> menuTree = buildMenuTree(tenantId, allowedMenuIds);

        // Build Response
        UserRbacResponse resp = new UserRbacResponse();
        resp.setUserId(userId);
        resp.setRoles(roleDTOs);
        resp.setPermissions(permissionDTOs);
        resp.setMenus(menuTree);

        return resp;
    }

    private List<MenuDTO> buildMenuTree(UUID tenantId, Set<UUID> allowedMenuIds) {

        List<Menu> menus = menuRepository.findByTenantId(tenantId);

        if (menus.isEmpty()) return List.of();

        Map<UUID, Menu> byId = menus.stream()
                .collect(Collectors.toMap(Menu::getId, m -> m));

        // Include all allowed menus + ancestors
        Set<UUID> include = new LinkedHashSet<>(allowedMenuIds);

        for (UUID mId : allowedMenuIds) {
            Menu cur = byId.get(mId);
            while (cur != null && cur.getParentMenu() != null) {
                UUID p = cur.getParentMenu().getId();
                if (!include.add(p)) break;
                cur = byId.get(p);
            }
        }

        // Convert to node map
        Map<UUID, MenuDTO> nodes = new HashMap<>();
        for (Menu m : menus) {
            if (!include.contains(m.getId())) continue;
            MenuDTO n = new MenuDTO();
            n.setId(m.getId());
            n.setName(m.getName());
            n.setUrl(m.getUrl());
            n.setIcon(m.getIcon());
            n.setTitle(m.getTitle());
            n.setBadge(m.getBadge());
            n.setIconComponent(m.getIconComponent());
            n.setChildren(new ArrayList<>());
            nodes.put(m.getId(), n);
        }

        // Build tree
        List<MenuDTO> roots = new ArrayList<>();
        for (UUID id : nodes.keySet()) {
            Menu m = byId.get(id);
            if (m.getParentMenu() != null && nodes.containsKey(m.getParentMenu().getId())) {
                nodes.get(m.getParentMenu().getId()).getChildren().add(nodes.get(id));
            } else {
                roots.add(nodes.get(id));
            }
        }
        return roots;
    }
}

