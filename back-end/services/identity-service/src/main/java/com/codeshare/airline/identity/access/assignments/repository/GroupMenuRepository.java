package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface GroupMenuRepository extends CSMDataBaseRepository<GroupMenu, Long> {

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
    List<Menu> findMenusByGroupsAndTenant(@Param("groups") List<Group> groups, @Param("tenantId") Long tenantId);

    @Query("select concat(gm.group.code, concat(':', gm.menu.code)) from GroupMenu gm where gm.tenantId = :tenantId")
    Set<String> findMappingsByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

    List<GroupMenu> findAllByTenantId(Long tenantId);

    boolean existsByTenantIdAndGroupAndMenu(Long tenantId, Group group, Menu menu);

    List<GroupMenu> findByGroup_Id(Long groupId);

    void deleteByGroup_Id(Long groupId);

    List<GroupMenu> findByMenu_Id(Long menuId);

    void deleteByMenu_Id(Long menuId);

}
