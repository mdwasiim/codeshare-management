package com.codeshare.airline.identity.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.Tenant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MenuRepository extends CSMDataBaseRepository<Menu, UUID> {

    List<Menu> findByTenant_TenantCode(String tenantCode);

    List<Menu> findByTenantIdAndParentMenuIsNull(UUID tenantId);

    boolean existsByTenant(Tenant tenant);

    @Query("select m.code from Menu m where m.tenant = :tenant")
    Set<String> findCodesByTenant(@Param("tenant") Tenant tenant);

    List<Menu> findByTenant(Tenant tenant);

    long countByTenantId(UUID tenantId);

}