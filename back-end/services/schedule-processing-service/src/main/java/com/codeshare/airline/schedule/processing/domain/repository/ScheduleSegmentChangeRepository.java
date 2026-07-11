package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleSegmentChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleSegmentChangeRepository extends CSMDataBaseRepository<ScheduleSegmentChangeEntity, UUID> {

    List<ScheduleSegmentChangeEntity> findByLegChangeId(UUID legChangeId);

    Optional<ScheduleSegmentChangeEntity> findByLegChangeIdAndBoardPointAndOffPoint(
            UUID legChangeId, String boardPoint, String offPoint);

    List<ScheduleSegmentChangeEntity> findByLegChangeIdAndSegmentChangeType(
            UUID legChangeId, SegmentChangeType segmentChangeType);

    List<ScheduleSegmentChangeEntity> findByMergeStatus(MergeStatus mergeStatus);

    List<ScheduleSegmentChangeEntity> findByLiveSegmentId(UUID liveSegmentId);
}
