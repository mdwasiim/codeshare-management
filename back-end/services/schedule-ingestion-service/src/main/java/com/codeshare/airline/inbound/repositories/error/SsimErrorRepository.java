package com.codeshare.airline.inbound.repositories.error;

import com.codeshare.airline.inbound.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.inbound.entities.error.SsimErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SsimErrorRepository
        extends JpaRepository<SsimErrorEntity, UUID> {

    List<ScheduleErrorEntity> findByFileId(UUID fileId);

}