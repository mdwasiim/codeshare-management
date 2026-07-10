package com.codeshare.airline.tenant.repository.ingestion;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.ingestion.ScheduleIngestionProfileEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleIngestionProfileRepository extends CSMDataBaseRepository<ScheduleIngestionProfileEntity, UUID> {

    Optional<ScheduleIngestionProfileEntity> findByTenant_TenantCode(String tenantCode);

    @Query("""
            select distinct p
            from ScheduleIngestionProfileEntity p
            join fetch p.tenant t
            left join fetch p.channels
            """)
    List<ScheduleIngestionProfileEntity> findAllWithChannels();

    @Query("""
            select p
            from ScheduleIngestionProfileEntity p
            join fetch p.tenant t
            left join fetch p.channels
            where t.tenantCode = :tenantCode
            """)
    Optional<ScheduleIngestionProfileEntity> findWithChannelsByTenantCode(String tenantCode);
}
