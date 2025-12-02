package com.codeshare.airline.tenant.repository;

import com.codeshare.airline.common.jpa.audit.BaseRepository;
import com.codeshare.airline.tenant.entities.DataSource;

import java.util.UUID;

public interface TenantDataSourceRepository extends BaseRepository<DataSource, UUID> {
}
