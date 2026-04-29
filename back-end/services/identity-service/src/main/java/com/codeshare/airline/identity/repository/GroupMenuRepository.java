package com.codeshare.airline.identity.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.GroupMenu;
import com.codeshare.airline.identity.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupMenuRepository extends CSMDataBaseRepository<GroupMenu, UUID> {

    @Query("""
    SELECT gm.menu
    FROM GroupMenu gm
    WHERE gm.group IN :groups
    """)
    List<Menu> findMenusByGroups(@Param("groups") List<Group> groups);

    @Query("""
    SELECT gm.menu
    FROM GroupMenu gm
    WHERE gm.group IN :groups
      AND gm.menu.tenant.tenantCode = :tenantCode
""")
    List<Menu> findMenusByGroupsAndTenant(
            @Param("groups") List<Group> groups,
            @Param("tenantCode") String tenantCode
    );
}