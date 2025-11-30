package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.UserRole;
import com.codeshare.airline.common.jpa.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends BaseRepository<UserRole, UUID> {

    List<UserRole> findByUserId(UUID userId);

    List<UserRole> findByRoleId(UUID roleId);

    boolean existsByUserIdAndRoleId(UUID userId, UUID roleId);

}
