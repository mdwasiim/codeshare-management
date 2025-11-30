package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.jpa.BaseRepository;
import com.codeshare.airline.tenant.entities.UserGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends BaseRepository<UserGroup, UUID> {
    List<UserGroup> findByTenantId(UUID tenantId);

    Optional<UserGroup> findByCode(String groupCode);
}
