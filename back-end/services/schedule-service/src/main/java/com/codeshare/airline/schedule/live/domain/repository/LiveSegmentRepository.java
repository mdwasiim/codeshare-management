package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveSegmentRepository extends CSMDataBaseRepository<LiveSegmentEntity, Long> {

    List<LiveSegmentEntity> findByFlightLegIdOrderByBoardPointAscOffPointAsc(Long flightLegId);

    Optional<LiveSegmentEntity> findByFlightLegIdAndBoardPointAndOffPoint(
            Long flightLegId,
            String boardPoint,
            String offPoint
    );

    void deleteByFlightLegId(Long flightLegId);
}
