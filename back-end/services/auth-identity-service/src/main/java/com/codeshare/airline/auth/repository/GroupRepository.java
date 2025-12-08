package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends BaseRepository<Group, UUID> {

    List<Group> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

    Group findByCodeAndTenantId(String code, UUID tenantId);


    Optional<Group>  findByTenantGroupId(UUID tenantGroupId);
}
