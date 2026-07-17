package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface GroupRoleRepository extends CSMDataBaseRepository<GroupRole, Long> {

    List<GroupRole> findByGroup_Id(Long groupId);

    List<GroupRole> findByRole_Id(Long groupId);

    boolean existsByGroup_IdAndRole_Id(Long groupId, Long roleId);

    @Query("select concat(gr.group.code, concat(':', gr.role.code)) from GroupRole gr where gr.tenantId = :tenantId")
    Set<String> findMappingsByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

    void deleteByGroup_Id(Long groupId);
}
