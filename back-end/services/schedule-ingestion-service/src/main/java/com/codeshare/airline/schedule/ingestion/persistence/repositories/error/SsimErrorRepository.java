package com.codeshare.airline.schedule.ingestion.persistence.repositories.error;

import com.codeshare.airline.schedule.ingestion.persistence.entities.error.SsimErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SsimErrorRepository
        extends JpaRepository<SsimErrorEntity, Long> {

    List<SsimErrorEntity> findByFileId(UUID fileId);

    long countByFileId(UUID fileId);

}
