package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRoleRepository extends JpaRepository<GroupRole, UUID> {
    Optional<GroupRole> findByName(String adminGroup);

    List<GroupRole> findByGroupId(UUID groupId);

    List<GroupRole> findByRoleId(UUID roleId);

    boolean existsByGroupIdAndRoleId(UUID groupId, UUID roleId);


}
