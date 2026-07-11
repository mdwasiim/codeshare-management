package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserGroupRepository extends CSMDataBaseRepository<UserGroup, UUID> {

    // Find all groups of a user
    List<UserGroup> findByUser_Id(UUID userId);

    boolean existsByTenantIdAndUser_IdAndGroup_Id(
            UUID tenantId,
            UUID userId,
            UUID groupId
    );

    @Query(value = """
    SELECT concat(user_id, ':', group_id)
    FROM auth_identity.user_groups
    WHERE tenant_id = :tenantId
""", nativeQuery = true)
    Set<String> findMappingsByTenantId(@Param("tenantId") UUID tenantId);

    @Query("""
    SELECT ug
    FROM UserGroup ug
    JOIN FETCH ug.group g
    WHERE ug.user.id = :userId
""")
    List<UserGroup> findByUserIdWithGroup(UUID userId);

    long countByTenantId(UUID tenantId);

    void deleteByUser_Id(UUID userId);
}
