package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.common.utils.UuidUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserLoader {

    private final UserRepository repo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    private static final List<String> TENANTS = List.of(
            "CSM","QAIR","EMIR","LUFTH","DELTA","AIND","SPJET","INDGO","UNITD","BAIR"
    );


    public void load() {

        for (String t : TENANTS) {

            UUID tenantId = UuidUtil.fixed("TENANT-" + t);
            UUID hq = UuidUtil.fixed("ORG-" + t + "-HQ");
            UUID it = UuidUtil.fixed("ORG-" + t + "-IT");

            create(t+"_admin", "Admin", t, tenantId, hq);
            create(t+"_manager", "Manager", t, tenantId, hq);
            create(t+"_staff", "Staff", t, tenantId, it);
        }
    }

    private void create(String username, String fn, String ln, UUID tenantId, UUID orgId) {
        repo.save(
                User.builder()
                        .id(UuidUtil.fixed("USER-" + username))
                        .username(username)
                        .firstName(fn)
                        .lastName(ln)
                        .email(username + "@example.com")
                        .password(passwordEncoder.encode("admin"))
                        .tenantId(tenantId)
                        .organizationId(orgId)
                        .enabled(true)
                        .accountNonLocked(true)
                        .accountNonLocked(false)
                        .build()
        );
    }
}
