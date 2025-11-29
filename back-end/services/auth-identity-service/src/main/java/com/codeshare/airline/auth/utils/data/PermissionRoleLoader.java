package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.PermissionRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PermissionRoleLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final PermissionRoleRepository repo;


    public void load() {

        List<String> tenants = List.of(
                "CSM","QAIR","EMIR","LUFTH","DELTA",
                "AIND","SPJET","INDGO","UNITD","BAIR"
        );

        List<String> perms_super = List.of(
                "user:create","user:update","user:delete",
                "role:create","group:create","booking:manage",
                "flight:manage","reports:generate","audit:view"
        );

        List<String> perms_tenant = List.of(
                "user:create","user:update","role:create","group:create"
        );

        List<String> perms_org = List.of(
                "booking:manage","flight:manage"
        );

        List<String> perms_manager = List.of(
                "reports:generate"
        );


        for (String t : tenants) {

            UUID sa = UuidUtil.fixed("ROLE-SUPER_ADMIN-" + t);
            UUID ta = UuidUtil.fixed("ROLE-TENANT_ADMIN-" + t);
            UUID oa = UuidUtil.fixed("ROLE-ORG_ADMIN-" + t);
            UUID mg = UuidUtil.fixed("ROLE-MANAGER-" + t);
            UUID st = UuidUtil.fixed("ROLE-STAFF-" + t);

            // SUPER ADMIN
            perms_super.forEach(code -> link(sa, code));

            // TENANT ADMIN
            perms_tenant.forEach(code -> link(ta, code));

            // ORG ADMIN
            perms_org.forEach(code -> link(oa, code));

            // MANAGER
            perms_manager.forEach(code -> link(mg, code));

            // STAFF
            link(st, "flight:manage");
        }
    }

    private void link(UUID roleId, String permCode) {

        Permission perm = permRepo.findById(UuidUtil.fixed("PERM-" + permCode)).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if (perm == null || role == null) return;

        UUID id = UuidUtil.fixed("PR-" + roleId + "-" + permCode);

        if (!repo.existsById(id)) {
            repo.save(PermissionRole.builder()
                    .id(id)
                    .role(role)
                    .permission(perm)
                    .build());
        }
    }
}
