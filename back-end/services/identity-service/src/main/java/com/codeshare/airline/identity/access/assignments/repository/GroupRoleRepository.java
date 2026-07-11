package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GroupRoleRepository extends CSMDataBaseRepository<GroupRole, UUID> {

    List<GroupRole> findByGroup_Id(UUID groupId);

    List<GroupRole> findByRole_Id(UUID groupId);

    boolean existsByGroup_IdAndRole_Id(UUID groupId, UUID roleId);

    @Query("select concat(gr.group.code, concat(':', gr.role.code)) from GroupRole gr where gr.tenantId = :tenantId")
    Set<String> findMappingsByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantId(UUID tenantId);

    void deleteByGroup_Id(UUID groupId);
}
