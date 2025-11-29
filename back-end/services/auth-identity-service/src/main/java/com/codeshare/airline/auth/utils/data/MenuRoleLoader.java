package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.MenuRoleRepository;
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
public class MenuRoleLoader {

    private final MenuRepository menuRepo;
    private final RoleRepository roleRepo;
    private final MenuRoleRepository repo;

    public void load() {

        if (menuRepo.count() == 0) return;

        Menu dashboard = menuRepo.findByCode("Dashboard").orElse(null);
        Menu users     = menuRepo.findByCode("USER_MGMT").orElse(null);

        if (dashboard == null) return;

        for (String t : List.of("CSM","QAIR","EMIR","LUFTH","DELTA",
                "AIND","SPJET","INDGO","UNITD","BAIR")) {

            UUID roleId = UuidUtil.fixed("ROLE-SUPER_ADMIN-" + t);

            link(roleId, dashboard);
            if (users != null) link(roleId, users);
        }
    }

    private void link(UUID roleId, Menu menu) {

        Role role = roleRepo.findById(roleId).orElse(null);
        if (role == null || menu == null) return;

        UUID id = UuidUtil.fixed("MR-" + roleId + "-" + menu.getId());

        if (!repo.existsById(id)) {
            repo.save(MenuRole.builder()
                    .id(id)
                    .role(role)
                    .menu(menu)
                    .build());
        }
    }
}
