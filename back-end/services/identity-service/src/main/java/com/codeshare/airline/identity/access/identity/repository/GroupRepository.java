package com.codeshare.airline.identity.access.identity.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.identity.entities.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupRepository extends CSMDataBaseRepository<Group, Long> {

    boolean existsByNameAndTenantId(String name, Long tenantId);

    List<Group> findByTenantId(Long tenantId);

    boolean existsByTenantIdAndCode(Long tenantId, String code);

    Optional<Group> findByNameAndTenantId(String groupName, Long tenantId);

    List<Group> findAllByTenantId(Long tenantId);

    Optional<Group> findByCodeAndTenantId(String code, Long tenantId);

    @Query("select g.code from Group g where g.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

}
