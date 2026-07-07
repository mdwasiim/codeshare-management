package com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.schedule.ingestion.dto.schedule.SchedulePeriodDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.SchedulePeriodEntity;
import org.springframework.stereotype.Component;

@Component
public class SchedulePeriodMapper {

    public SchedulePeriodEntity toEntity(SchedulePeriodDTO dto) {
        if (dto == null) return null;

        SchedulePeriodEntity entity = new SchedulePeriodEntity();

        /* ================= VALIDATION ================= */

        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        /* ================= CORE ================= */

        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        entity.setDaysOfOperation(
                dto.getDaysOfOperation() != null ? dto.getDaysOfOperation().trim() : null
        );

        entity.setFrequencyRate(
                dto.getFrequencyRate() != null ? dto.getFrequencyRate() : 0
        );

        return entity;
    }

    public SchedulePeriodDTO toDTO(SchedulePeriodEntity entity) {
        if (entity == null) return null;

        SchedulePeriodDTO dto = new SchedulePeriodDTO();
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDaysOfOperation(entity.getDaysOfOperation());
        dto.setFrequencyRate(entity.getFrequencyRate());
        return dto;
    }
}
