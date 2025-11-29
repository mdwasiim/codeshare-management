package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final GroupLoader groupLoader;
    private final GroupRoleLoader groupRoleLoader;
    private final MenuLoader menuLoader;
    private final MenuRoleLoader menuRoleLoader;
    private final PermissionLoader permissionLoader;
    private final PermissionRoleLoader permissionRoleLoader;
    private final RoleLoader roleLoader;
    private final UserGroupRoleLoader userGroupRoleLoader;
    private final UserLoader userLoader;
    private final UserRoleLoader userRoleLoader;
    
    private final RoleRepository repo;


    @PostConstruct
    @Transactional
    public void load() throws JsonProcessingException {
        long count = repo.count();
        if(count==0){
            roleLoader.load();
            permissionLoader.load();
            groupLoader.load();
            menuLoader.load();
            userLoader.load();

            groupRoleLoader.load();
            menuRoleLoader.load();
            permissionRoleLoader.load();
            userRoleLoader.load();
            userGroupRoleLoader.load();
        }
    }
}
