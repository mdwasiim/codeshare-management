package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.services.jpa.BaseRepository;
import com.codeshare.airline.tenant.entities.TenantDataSource;

import java.util.UUID;

public interface TenantDataSourceRepository extends BaseRepository<TenantDataSource, UUID> {
}
