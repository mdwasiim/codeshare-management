package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.tenant.entities.TenantDataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantDataSourceRepository extends JpaRepository<TenantDataSource, UUID> {
}
