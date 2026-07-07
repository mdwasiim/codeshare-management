package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleCodeshareChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.CodeshareChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleCodeshareChangeRepository extends CSMDataBaseRepository<ScheduleCodeshareChangeEntity, UUID> {

    List<ScheduleCodeshareChangeEntity> findByLegChangeId(UUID legChangeId);

    List<ScheduleCodeshareChangeEntity> findByLegChangeIdAndChangeType(UUID legChangeId, CodeshareChangeType changeType);

    List<ScheduleCodeshareChangeEntity> findByMergeStatus(MergeStatus mergeStatus);

    List<ScheduleCodeshareChangeEntity> findByLiveCodeshareId(UUID liveCodeshareId);
}
