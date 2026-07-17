package com.codeshare.airline.schedule.compare.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.compare.domain.entity.ScheduleCodeshareChangeEntity;
import com.codeshare.airline.schedule.compare.domain.enums.CodeshareChangeType;
import com.codeshare.airline.schedule.compare.domain.enums.ChangeSetStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleCodeshareChangeRepository extends CSMDataBaseRepository<ScheduleCodeshareChangeEntity, Long> {

    List<ScheduleCodeshareChangeEntity> findByLegChangeId(Long legChangeId);

    List<ScheduleCodeshareChangeEntity> findByLegChangeIdAndChangeType(Long legChangeId, CodeshareChangeType changeType);

    List<ScheduleCodeshareChangeEntity> findByChangeSetStatus(ChangeSetStatus changeSetStatus);

    List<ScheduleCodeshareChangeEntity> findByLiveCodeshareId(Long liveCodeshareId);
}

