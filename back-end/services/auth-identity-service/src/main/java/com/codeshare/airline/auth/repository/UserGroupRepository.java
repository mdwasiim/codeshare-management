package com.codeshare.airline.auth.repository;


import com.codeshare.airline.auth.entities.UserGroup;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface UserGroupRepository extends CSMDataBaseRepository<UserGroup, UUID> {

    // Find all groups of a user
    List<UserGroup> findByUser_Id(UUID userId);

    boolean existsByTenant_IdAndUser_IdAndGroup_Id(
            UUID tenantId,
            UUID userId,
            UUID groupId
    );

}
