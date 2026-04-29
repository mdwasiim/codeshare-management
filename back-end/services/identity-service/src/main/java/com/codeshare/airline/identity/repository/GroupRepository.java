package com.codeshare.airline.identity.repository;


import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends CSMDataBaseRepository<Group, UUID> {

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    List<Group> findByTenant(Tenant tenant);

    boolean existsByTenantAndCode(Tenant tenant, String code);

    List<Group> findByTenantId(UUID tenantId);

    Optional<Group> findByNameAndTenant_Id(String groupName, UUID tenantId);

    List<Group> findAllByTenant_Id(UUID tenantId);

    Optional<Group> findByCodeAndTenant_TenantCode(String code, String tenantCode);

}
