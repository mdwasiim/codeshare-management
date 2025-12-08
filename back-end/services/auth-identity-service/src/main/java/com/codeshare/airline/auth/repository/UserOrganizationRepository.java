package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.UserOrganization;
import com.codeshare.airline.common.auth.identity.model.UserOrganizationDTO;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface UserOrganizationRepository extends BaseRepository<UserOrganization, UUID> {

    List<UserOrganization> findByOrganizationId(UUID organizationId);
    List<UserOrganization> findByUserId(UUID userId);
    List<UserOrganization> findByUserIdAndTenantId(UUID userId, UUID tenantId);

    UserOrganization assign(UserOrganizationDTO dto);

    List<UserOrganization> getByUserId(UUID userId);

    List<UserOrganization> getByOrganization(UUID organizationId);

    UserOrganization setPrimary(UUID mappingId);

    void remove(UUID mappingId);

    boolean existsByUser_IdAndOrganizationId(UUID userId, UUID organizationId);

    UserOrganization findByUserIdAndPrimaryTrue(UUID id);
}
