package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.repository.UserRoleRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRoleLoader {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository repo;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA",
            "AIND","SPJET","INDGO","UNITD","BAIR"
    );


    public void load() {

        for (String t : TENANTS) {

            UUID tenantId = UuidUtil.fixed("TENANT-" + t);

            assign(t + "_admin",   "TENANT_ADMIN", tenantId, t);
            assign(t + "_manager", "ORG_ADMIN",    tenantId, t);
            assign(t + "_staff",   "STAFF",        tenantId, t);
        }
    }

    private void assign(String username, String roleCode, UUID tenantId, String t) {

        UUID userId = UuidUtil.fixed("USER-" + username);
        UUID roleId = UuidUtil.fixed("ROLE-" + roleCode + "-" + t);

        User user = userRepo.findById(userId).orElse(null);
        Role role = roleRepo.findById(roleId).orElse(null);

        if (user == null || role == null) return;

        UUID id = UuidUtil.fixed("UR-" + userId + "-" + roleId);

        if (!repo.existsById(id)) {
            repo.save(UserRole.builder()
                    .id(id)
                    .user(user)
                    .role(role)
                    .build());
        }
    }
}
