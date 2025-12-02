package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.common.jpa.audit.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends BaseRepository<Group, UUID> {

    List<Group> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);


}
