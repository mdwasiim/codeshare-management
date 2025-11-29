package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.GroupRoleRepository;
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
public class GroupRoleLoader {

    private final GroupRepository groupRepo;
    private final RoleRepository roleRepo;
    private final GroupRoleRepository repo;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA",
            "AIND","SPJET","INDGO","UNITD","BAIR"
    );

    public void load() {

        for (String t : TENANTS) {

            assign(t,"ADMIN","TENANT_ADMIN");
            assign(t,"IT","ORG_ADMIN");
            assign(t,"OPS","STAFF");
        }
    }

    private void assign(String t, String groupName, String roleCode) {

        UUID groupId = UuidUtil.fixed("GROUP-" + t + "-" + groupName);
        UUID roleId  = UuidUtil.fixed("ROLE-" + roleCode + "-" + t);

        groupRepo.findById(groupId).ifPresent(g -> {
            roleRepo.findById(roleId).ifPresent(r -> {

                UUID id = UuidUtil.fixed("GR-" + groupId + "-" + roleId);

                if (!repo.existsById(id)) {
                    repo.save(GroupRole.builder()
                            //.id(id)
                            .group(g)
                            .role(r)
                            .build());
                }
            });
        });
    }
}
