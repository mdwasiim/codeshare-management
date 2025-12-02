package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.common.jpa.audit.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends BaseRepository<Role, UUID> {



    Optional<Role> findByName(String admin);

    @Query("SELECT r.name FROM Role r WHERE r.id = :roleId")
    String findNameById(@Param("roleId") UUID roleId);

    List<Role> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);

}
