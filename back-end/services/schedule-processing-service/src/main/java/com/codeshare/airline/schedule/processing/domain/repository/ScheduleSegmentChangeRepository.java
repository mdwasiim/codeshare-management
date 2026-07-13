package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleSegmentChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSegmentChangeRepository extends CSMDataBaseRepository<ScheduleSegmentChangeEntity, Long> {

    List<ScheduleSegmentChangeEntity> findByLegChangeId(Long legChangeId);

    Optional<ScheduleSegmentChangeEntity> findByLegChangeIdAndBoardPointAndOffPoint(
            Long legChangeId, String boardPoint, String offPoint);

    List<ScheduleSegmentChangeEntity> findByLegChangeIdAndSegmentChangeType(
            Long legChangeId, SegmentChangeType segmentChangeType);

    List<ScheduleSegmentChangeEntity> findByChangeSetStatus(ChangeSetStatus changeSetStatus);

    List<ScheduleSegmentChangeEntity> findByLiveSegmentId(Long liveSegmentId);
}

