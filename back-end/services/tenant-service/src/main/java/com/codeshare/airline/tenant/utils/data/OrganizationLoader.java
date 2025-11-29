package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.common.utils.UuidUtil;
import com.codeshare.airline.tenant.entities.Organization;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.OrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizationLoader {

    private final OrganizationRepository repo;
    private final TenantRepository tenantRepository;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA",
            "AIND","SPJET","INDGO","UNITD","BAIR"
    );


    public void organizationLoad() {

        for (String t : TENANTS) {

            UUID tenantId = UuidUtil.fixed("TENANT-" + t);

            Tenant tenant =tenantRepository.findById(tenantId).orElse(null);

            // -------- HQ Organization --------
            UUID hqId = UuidUtil.fixed("ORG-" + t + "-HQ");

            if (!repo.existsById(hqId)) {
                Organization org = Organization.builder()
                        .id(hqId)
                        .tenant(tenant)
                        .code(t + "-HQ")
                        .name("Headquarters")
                        .description("Main corporate office for tenant " + t)
                        .active(true)
                        .build();

                repo.save(org);
            }

            // -------- IT Organization --------
            UUID itId = UuidUtil.fixed("ORG-" + t + "-IT");

            // (!repo.existsById(itId)) {
                Organization org = Organization.builder()
                        .id(itId)
                        .tenant(tenant)
                        .code(t + "-IT")
                        .name("IT Department")
                        .description("Technology and Support division of " + t)
                        .active(true)
                        .build();

                repo.save(org);
            //}
        }
    }
}
