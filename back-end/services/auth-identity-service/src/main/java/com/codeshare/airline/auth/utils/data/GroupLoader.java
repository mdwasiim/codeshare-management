package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GroupLoader {

    private final GroupRepository repo;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA",
            "AIND","SPJET","INDGO","UNITD","BAIR"
    );


    public void load() {

        for (String t : TENANTS) {

            UUID tenantId = UuidUtil.fixed("TENANT-" + t);

            createIfMissing(
                    UuidUtil.fixed("GROUP-" + t + "-ADMIN"),
                    tenantId,
                    t + "-ADMIN",
                    "Admin Group for " + t
            );

            createIfMissing(
                    UuidUtil.fixed("GROUP-" + t + "-IT"),
                    tenantId,
                    t + "-IT",
                    "IT Group for " + t
            );

            createIfMissing(
                    UuidUtil.fixed("GROUP-" + t + "-OPS"),
                    tenantId,
                    t + "-OPS",
                    "Operations Group for " + t
            );
        }
    }

    private void createIfMissing(UUID id, UUID tenantId, String code, String name) {

            Group g = Group.builder()
                    .id(UuidUtil.idFor(code))
                    .code(code)
                    .name(name)
                    .tenantId(tenantId)
                    .build();
             repo.saveAndFlush(g);

    }
}
