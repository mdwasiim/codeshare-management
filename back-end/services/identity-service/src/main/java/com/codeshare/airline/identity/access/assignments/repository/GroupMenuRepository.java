package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
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
      AND gm.menu.tenantId = :tenantId
""")
    List<Menu> findMenusByGroupsAndTenant(@Param("groups") List<Group> groups, @Param("tenantId") UUID tenantId);

    @Query("select concat(gm.group.code, concat(':', gm.menu.code)) from GroupMenu gm where gm.tenantId = :tenantId")
    Set<String> findMappingsByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantId(UUID tenantId);

    List<GroupMenu> findAllByTenantId(UUID tenantId);

    boolean existsByTenantIdAndGroupAndMenu(UUID tenantId, Group group, Menu menu);

    List<GroupMenu> findByGroup_Id(UUID groupId);

    void deleteByGroup_Id(UUID groupId);

    List<GroupMenu> findByMenu_Id(UUID menuId);

    void deleteByMenu_Id(UUID menuId);

}
