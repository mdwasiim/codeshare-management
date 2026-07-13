package com.codeshare.airline.identity.access.identity.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends CSMDataBaseRepository<Role, Long> {

    List<Role> findByTenantId(Long tenantId);

    boolean existsByNameAndTenantId(String name, Long tenantId);

    boolean existsByTenantIdAndCode(Long tenantId, String code);

    @Query("select r.code from Role r where r.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);
}
