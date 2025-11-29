package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleLoader {

    private final RoleRepository repo;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA","AIND","SPJET","INDGO","UNITD","BAIR"
    );

    private static final List<String> BASE_ROLES = List.of(
            "SUPER_ADMIN","TENANT_ADMIN","ORG_ADMIN","MANAGER","STAFF"
    );


    public void load() {

        for (String t : TENANTS) {
            UUID tenantId = UuidUtil.fixed("TENANT-" + t);

            for (String role : BASE_ROLES) {
                repo.save(
                        Role.builder()
                                .id(UuidUtil.fixed("ROLE-" + role + "-" + t))
                                .tenantId(tenantId)
                                .code(role)
                                .name(role.replace("_"," "))
                                .build()
                );
            }
        }
    }
}
