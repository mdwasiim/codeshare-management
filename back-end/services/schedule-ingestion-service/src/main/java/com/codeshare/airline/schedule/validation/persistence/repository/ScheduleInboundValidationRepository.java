package com.codeshare.airline.schedule.validation.persistence.repository;

import com.codeshare.airline.schedule.validation.persistence.entity.ScheduleInboundValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleInboundValidationRepository
        extends JpaRepository<ScheduleInboundValidation, UUID> {

    List<ScheduleInboundValidation> findByFileId(UUID fileId);

    List<ScheduleInboundValidation> findByFileIdAndBlockingTrue(UUID fileId);
}