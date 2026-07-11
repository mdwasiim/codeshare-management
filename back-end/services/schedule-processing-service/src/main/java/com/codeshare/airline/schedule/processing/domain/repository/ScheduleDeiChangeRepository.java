package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.DeiScope;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleDeiChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.DeiChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.MergeStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleDeiChangeRepository extends CSMDataBaseRepository<ScheduleDeiChangeEntity, UUID> {

    List<ScheduleDeiChangeEntity> findByLegChangeId(UUID legChangeId);

    Optional<ScheduleDeiChangeEntity> findByLegChangeIdAndDeiCodeAndSequenceOrder(
            UUID legChangeId,
            String deiCode,
            Integer sequenceOrder
    );

    List<ScheduleDeiChangeEntity> findBySegmentChangeId(UUID segmentChangeId);

    Optional<ScheduleDeiChangeEntity> findBySegmentChangeIdAndDeiCodeAndSequenceOrder(
            UUID segmentChangeId,
            String deiCode,
            Integer sequenceOrder
    );

    List<ScheduleDeiChangeEntity> findByDeiScope(DeiScope deiScope);

    List<ScheduleDeiChangeEntity> findByDeiChangeTypeAndMergeStatus(DeiChangeType deiChangeType, MergeStatus mergeStatus);

    List<ScheduleDeiChangeEntity> findByMergeStatus(MergeStatus mergeStatus);
}
