package com.codeshare.airline.auth.repository;


import com.codeshare.airline.auth.entities.rbac.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

    List<UserGroup> findByUserId(UUID userId);

    List<UserGroup> findByGroupId(UUID groupId);

    boolean existsByUserIdAndGroupId(UUID userId, UUID groupId);

    void deleteByUserIdAndGroupId(UUID userId, UUID groupId);

    void deleteByGroupId(UUID id);

    Optional<UserGroup> findByUserIdAndGroupId(UUID userId, UUID groupId);
}
