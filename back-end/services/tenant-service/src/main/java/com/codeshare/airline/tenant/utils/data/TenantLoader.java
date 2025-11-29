package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.common.utils.UuidUtil;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantLoader {

    private final TenantRepository repo;
    private final TenantDataSourceRepository dsRepo;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA","AIND","SPJET","INDGO","UNITD","BAIR"
    );

    public void teanantLoader() {

        for (String code : TENANTS) {

            UUID id = UuidUtil.fixed("TENANT-" + code);

            repo.findById(id).orElseGet(() -> repo.save(
                    Tenant.builder()
                            .id(id)
                            .code(code)
                            .name(code + " Tenant")
                            .description("Default tenant " + code)
                            .plan("PRO")
                            .trial(false)
                            .enabled(true)
                            .dbConfig(dsRepo.findById(UuidUtil.fixed("DS-MYSQL_PRIMARY")).orElse(null))
                            .contactEmail(code.toLowerCase() + "@example.com")
                            .region("GLOBAL")
                            .createdBy("SYSTEM")
                            .build()
            ));
        }
    }
}

