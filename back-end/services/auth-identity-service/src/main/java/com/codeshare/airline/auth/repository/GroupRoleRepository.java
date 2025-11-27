package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRoleRepository extends JpaRepository<GroupRole, UUID> {
    Optional<GroupRole> findByGroup_Name(String adminGroup);

    List<GroupRole> findByGroup_Id(UUID groupId);

    List<GroupRole> findByRole_Id(UUID roleId);

    boolean existsByGroup_IdAndRole_Id(UUID groupId, UUID roleId);


}
