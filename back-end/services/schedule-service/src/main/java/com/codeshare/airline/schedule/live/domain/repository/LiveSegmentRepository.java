package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveSegmentRepository extends CSMDataBaseRepository<LiveSegmentEntity, UUID> {

    List<LiveSegmentEntity> findByFlightLegIdOrderByBoardPointAscOffPointAsc(UUID flightLegId);

    Optional<LiveSegmentEntity> findByFlightLegIdAndBoardPointAndOffPoint(
            UUID flightLegId,
            String boardPoint,
            String offPoint
    );

    void deleteByFlightLegId(UUID flightLegId);
}
