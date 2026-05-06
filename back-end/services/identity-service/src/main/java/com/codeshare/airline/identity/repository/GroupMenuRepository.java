package com.codeshare.airline.identity.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.GroupMenu;
import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.Tenant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
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
    List<Menu> findMenusByGroupsAndTenant( @Param("groups") List<Group> groups,@Param("tenantCode") String tenantCode);

    @Query("select gm.group.code || ':' || gm.menu.code from GroupMenu gm where gm.tenant = :tenant")
    Set<String> findMappings(@Param("tenant") Tenant tenant);

    long countByTenantId(UUID tenantId);

    List<Group> findAllByTenant_Id(UUID tenantId);

    boolean existsByTenantAndGroupAndMenu(
            Tenant tenant,
            Group group,
            Menu menu
    );

}