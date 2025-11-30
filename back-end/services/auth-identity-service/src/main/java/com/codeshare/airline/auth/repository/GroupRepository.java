package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.common.jpa.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends BaseRepository<Group, UUID> {

    List<Group> findByTenantId(UUID tenantId);

    boolean existsByNameAndTenantId(String name, UUID tenantId);


}
