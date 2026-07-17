package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.DeiScope;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleDeiChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.DeiChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleDeiChangeRepository extends CSMDataBaseRepository<ScheduleDeiChangeEntity, Long> {

    List<ScheduleDeiChangeEntity> findByLegChangeId(Long legChangeId);

    Optional<ScheduleDeiChangeEntity> findByLegChangeIdAndDeiCodeAndSequenceOrder(
            Long legChangeId,
            String deiCode,
            Integer sequenceOrder
    );

    List<ScheduleDeiChangeEntity> findBySegmentChangeId(Long segmentChangeId);

    Optional<ScheduleDeiChangeEntity> findBySegmentChangeIdAndDeiCodeAndSequenceOrder(
            Long segmentChangeId,
            String deiCode,
            Integer sequenceOrder
    );

    List<ScheduleDeiChangeEntity> findByDeiScope(DeiScope deiScope);

    List<ScheduleDeiChangeEntity> findByDeiChangeTypeAndChangeSetStatus(DeiChangeType deiChangeType, ChangeSetStatus changeSetStatus);

    List<ScheduleDeiChangeEntity> findByChangeSetStatus(ChangeSetStatus changeSetStatus);
}

