package com.codeshare.airline.identity.access.authorization.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MenuRepository extends CSMDataBaseRepository<Menu, UUID> {

    List<Menu> findByTenantId(UUID tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    List<Menu> findByTenantIdOrderByDisplayOrderAscCodeAsc(UUID tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNullOrderByDisplayOrderAscCodeAsc(UUID tenantId);

    boolean existsByTenantId(UUID tenantId);

    @Query("select m.code from Menu m where m.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantId(UUID tenantId);

}
