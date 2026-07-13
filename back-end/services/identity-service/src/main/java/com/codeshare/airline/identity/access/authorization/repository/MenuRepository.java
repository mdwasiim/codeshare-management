package com.codeshare.airline.identity.access.authorization.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends CSMDataBaseRepository<Menu, Long> {

    List<Menu> findByTenantId(Long tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNull(Long tenantId);

    List<Menu> findByTenantIdOrderByDisplayOrderAscCodeAsc(Long tenantId);

    List<Menu> findByTenantIdAndParentMenuIsNullOrderByDisplayOrderAscCodeAsc(Long tenantId);

    boolean existsByTenantId(Long tenantId);

    @Query("select m.code from Menu m where m.tenantId = :tenantId")
    Set<String> findCodesByTenantId(@Param("tenantId") Long tenantId);

    long countByTenantId(Long tenantId);

}
