package com.codeshare.airline.identity.repository;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.entities.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserGroupRepository extends CSMDataBaseRepository<UserGroup, UUID> {

    // Find all groups of a user
    List<UserGroup> findByUser_Id(UUID userId);

    boolean existsByTenant_IdAndUser_IdAndGroup_Id(
            UUID tenantId,
            UUID userId,
            UUID groupId
    );

    boolean existsByTenantAndUserAndGroup(Tenant tenant, User user, Group group);

    @Query("select ug.user.id || ':' || ug.group.id from UserGroup ug where ug.tenant = :tenant")
    Set<String> findMappings(@Param("tenant") Tenant tenant);

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

    void deleteByUser_Id(UUID userId);
}
