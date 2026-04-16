package com.codeshare.airline.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.ingestion.domain.enums.DeiScope;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;
import org.springframework.stereotype.Component;

@Component
public class ScheduleDataElementMapper {

    public ScheduleDataElementEntity toEntity(ScheduleDataElementDTO dto) {
        if (dto == null) return null;

        ScheduleDataElementEntity entity = new ScheduleDataElementEntity();

        /* ================= CORE ================= */

        if (dto.getDeiCode() == null) {
            throw new IllegalArgumentException("DEI code is mandatory");
        }
        entity.setDeiCode(dto.getDeiCode());

        entity.setValue(
                dto.getValue() != null ? dto.getValue().trim() : null
        );

        entity.setSequenceOrder(
                dto.getSequenceOrder() != null ? dto.getSequenceOrder() : 0
        );

        /* ================= SCOPE ================= */

        if (dto.getScope() != null) {
            entity.setScope(dto.getScope());
        } else if (dto.isFlightLevel()) {
            entity.setScope(DeiScope.FLIGHT);
        } else if (dto.isLegLevel()) {
            entity.setScope(DeiScope.LEG);
        } else if (dto.isSegmentLevel()) {
            entity.setScope(DeiScope.SEGMENT);
        } else {
            throw new IllegalArgumentException("DEI scope cannot be determined for code: " + dto.getDeiCode());
        }

        /* ================= SEGMENT CONTEXT ================= */

        if (entity.getScope() == DeiScope.SEGMENT) {
            entity.setBoardPoint(dto.getBoardPoint());
            entity.setOffPoint(dto.getOffPoint());
        } else {
            entity.setBoardPoint(null);
            entity.setOffPoint(null);
        }

        return entity;
    }

    public ScheduleDataElementDTO toDTO(ScheduleDataElementEntity entity) {
        if (entity == null) return null;

        ScheduleDataElementDTO dto = new ScheduleDataElementDTO();

        dto.setDeiCode(entity.getDeiCode());
        dto.setValue(entity.getValue());
        dto.setSequenceOrder(entity.getSequenceOrder());
        dto.setScope(entity.getScope());

        dto.setBoardPoint(entity.getBoardPoint());
        dto.setOffPoint(entity.getOffPoint());

        return dto;
    }
}