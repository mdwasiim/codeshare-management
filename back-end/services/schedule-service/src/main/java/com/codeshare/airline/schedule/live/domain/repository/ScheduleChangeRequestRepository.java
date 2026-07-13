package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.ApprovalStatus;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.ScheduleChangeRequestEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleChangeRequestRepository extends CSMDataBaseRepository<ScheduleChangeRequestEntity, Long> {

    Optional<ScheduleChangeRequestEntity> findByChangeSetId(UUID changeSetId);

    List<ScheduleChangeRequestEntity> findByApprovalStatusOrderByCreatedAtAsc(ApprovalStatus approvalStatus);
}
