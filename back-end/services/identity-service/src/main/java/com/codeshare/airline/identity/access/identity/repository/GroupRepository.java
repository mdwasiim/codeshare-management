package com.codeshare.airline.identity.access.identity.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupRepository extends CSMDataBaseRepository<Group, UUID> {

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    List<Group> findByTenantId(UUID tenantId);

    boolean existsByTenantIdAndCode(UUID tenantId, String code);

    Optional<Group> findByNameAndTenantId(String groupName, UUID tenantId);

    List<Group> findAllByTenantId(UUID tenantId);

    Optional<Group> findByCodeAndTenantId(String code, UUID tenantId);

    @Query("select g.code from Group g where g.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantId(UUID tenantId);

}
