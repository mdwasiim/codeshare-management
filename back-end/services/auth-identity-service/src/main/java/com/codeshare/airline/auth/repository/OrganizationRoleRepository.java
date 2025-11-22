package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authorization.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole, UUID> {

    List<OrganizationRole> findByOrganizationId(UUID organizationId);

}
