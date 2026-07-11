package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveSsmMessageEntity;
import com.codeshare.airline.schedule.live.domain.enums.SsmOutboundStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LiveSsmMessageRepository extends CSMDataBaseRepository<LiveSsmMessageEntity, UUID> {

    List<LiveSsmMessageEntity> findByOutboundStatusOrderByCreatedAtAsc(SsmOutboundStatus status);

    List<LiveSsmMessageEntity> findByFlightLegIdOrderByCreatedAtDesc(UUID flightLegId);

    List<LiveSsmMessageEntity> findByAirlineCodeAndFlightNumberAndMessageType(
            String airlineCode,
            String flightNumber,
            MessageType messageType
    );

    @Query("""
            SELECT m FROM LiveSsmMessageEntity m
            WHERE m.outboundStatus IN ('PENDING', 'RETRYING')
            ORDER BY m.createdAt ASC
            """)
    List<LiveSsmMessageEntity> findAllPendingForDispatch();

    @Query("""
            SELECT m FROM LiveSsmMessageEntity m
            WHERE m.outboundStatus = 'FAILED'
              AND m.retryCount < :maxRetries
            ORDER BY m.lastRetryAt ASC NULLS FIRST
            """)
    List<LiveSsmMessageEntity> findRetryableMessages(@Param("maxRetries") int maxRetries);
}
