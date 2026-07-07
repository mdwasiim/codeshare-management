package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentDeiEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LiveSegmentDeiRepository extends CSMDataBaseRepository<LiveSegmentDeiEntity, UUID> {

    List<LiveSegmentDeiEntity> findBySegmentIdOrderBySequenceOrderAsc(UUID segmentId);

    List<LiveSegmentDeiEntity> findBySegmentIdAndDataElementIdentifier(
            UUID segmentId,
            String dataElementIdentifier
    );

    void deleteBySegmentId(UUID segmentId);
}
