package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PermissionLoader {

    private final PermissionRepository repo;

    private static final List<String> PERMS = List.of(
            "user:create","user:update","user:delete",
            "group:create","role:create",
            "booking:manage","flight:manage",
            "reports:generate","audit:view"
    );

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA",
            "AIND","SPJET","INDGO","UNITD","BAIR"
    );

    public void load() {

        for (String t : TENANTS) {

            UUID tenantId = UuidUtil.fixed("TENANT-" + t);
            for (String code : PERMS) {
                repo.save(
                        Permission.builder()
                                .id(UuidUtil.fixed("PERM-" + code))
                                .code(code)
                                .name(code)
                                .tenantId(tenantId)  // global
                                .build()
                );
            }
        }


    }
}
