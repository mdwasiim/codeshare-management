package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.UserGroupRoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserGroupRoleLoader {

    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final UserGroupRoleRepository repo;

    public void load() {

        List<String> tenants = List.of(
                "CSM","QAIR","EMIR","LUFTH","DELTA",
                "AIND","SPJET","INDGO","UNITD","BAIR"
        );

        for (String t : tenants) {

            assign(t+"_admin",   t,"ADMIN");
            assign(t+"_manager", t,"IT");
            assign(t+"_staff",   t,"OPS");
        }
    }

    private void assign(String username, String t, String gname) {

        UUID userId = UuidUtil.fixed("USER-" + username);
        UUID groupId = UuidUtil.fixed("GROUP-" + t + "-" + gname);

        userRepo.findById(userId).ifPresent(u -> {
            groupRepo.findById(groupId).ifPresent(g -> {

                UUID id = UuidUtil.fixed("UGR-" + userId + "-" + groupId);

                if (!repo.existsById(id)) {
                    repo.save(UserGroupRole.builder()
                            .id(id)
                            .user(u)
                            .group(g)
                            .build());
                }
            });
        });
    }
}

