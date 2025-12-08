package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.tenant.entities.TenantOrganizationGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantOrganizationGroupUserRepository extends JpaRepository<TenantOrganizationGroupUser, UUID> {

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    Optional<TenantOrganizationGroupUser> findByGroupIdAndUserId(UUID groupId, UUID userId);

    List<TenantOrganizationGroupUser> findByGroupId(UUID groupId);

    List<TenantOrganizationGroupUser> findByUserId(UUID userId);

}
