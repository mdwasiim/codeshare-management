package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentDeiEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveSegmentDeiRepository extends CSMDataBaseRepository<LiveSegmentDeiEntity, Long> {

    List<LiveSegmentDeiEntity> findBySegmentIdOrderBySequenceOrderAsc(Long segmentId);

    List<LiveSegmentDeiEntity> findBySegmentIdAndDataElementIdentifier(
            Long segmentId,
            String dataElementIdentifier
    );

    void deleteBySegmentId(Long segmentId);
}
