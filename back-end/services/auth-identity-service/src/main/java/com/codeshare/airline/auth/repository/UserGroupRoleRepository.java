package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, UUID> {

    List<UserGroupRole> findByUserId(UUID userId);

    List<UserGroupRole> findByGroupId(UUID groupId);

    List<UserGroupRole> findByRoleId(UUID roleId);

    boolean existsByUserIdAndGroupIdAndRoleId(UUID userId, UUID groupId, UUID roleId);
}
