package com.codeshare.airline.identity.access.assignments.repository;


import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserGroupRepository extends CSMDataBaseRepository<UserGroup, Long> {

    // Find all groups of a user
    List<UserGroup> findByUser_Id(Long userId);

    // Find all users of a group
    List<UserGroup> findByGroup_Id(Long groupId);

    boolean existsByTenantIdAndUser_IdAndGroup_Id(
            Long tenantId,
            Long userId,
            Long groupId
    );

    @Query(value = """
    SELECT concat(user_id, ':', group_id)
    FROM auth_identity.user_groups
    WHERE tenant_id = :tenantId
""", nativeQuery = true)
    Set<String> findMappingsByTenantId(@Param("tenantId") Long tenantId);

    @Query("""
    SELECT ug
    FROM UserGroup ug
    JOIN FETCH ug.group g
    WHERE ug.user.id = :userId
""")
    List<UserGroup> findByUserIdWithGroup(Long userId);

    long countByTenantId(Long tenantId);

    void deleteByUser_Id(Long userId);

    void deleteByGroup_Id(Long groupId);
}
