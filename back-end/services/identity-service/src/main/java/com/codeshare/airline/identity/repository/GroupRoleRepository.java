package com.codeshare.airline.identity.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.GroupRole;
import com.codeshare.airline.identity.entities.Role;
import com.codeshare.airline.identity.entities.Tenant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GroupRoleRepository extends CSMDataBaseRepository<GroupRole, UUID> {

    List<GroupRole> findByGroup_Id(UUID groupId);

    List<GroupRole> findByRole_Id(UUID groupId);

    boolean existsByGroup_IdAndRole_Id(UUID groupId, UUID roleId);

    boolean existsByTenantAndGroupAndRole(Tenant tenant, Group group, Role role);

    @Query("select gr.group.code || ':' || gr.role.code from GroupRole gr where gr.tenant = :tenant")
    Set<String> findMappings(@Param("tenant") Tenant tenant);

    long countByTenantId(UUID tenantId);

    void deleteByGroup_Id(UUID groupId);
}
