package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    List<UserRole> findByUserId(UUID userId);

    List<UserRole> findByRoleId(UUID roleId);

    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);

}
