package com.codeshare.airline.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.ingestion.persistence.mappers.BaseScheduleFileMapper;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFileMetaDataMapper extends BaseScheduleFileMapper {

    public ScheduleFileMetaDataEntity toEntity(ScheduleFileMetaDataDTO dto) {
        if (dto == null) return null;

        ScheduleFileMetaDataEntity entity = new ScheduleFileMetaDataEntity();
        mapToEntity(dto, entity);
        return entity;
    }

    /* =========================================================
       ENTITY → DTO (TYPE-SAFE METHODS)
       ========================================================= */
    public ScheduleFileMetaDataDTO toDto(ScheduleFileMetaDataEntity entity) {
        if (entity == null) return null;

        ScheduleFileMetaDataDTO.ScheduleFileMetaDataDTOBuilder<?, ?> builder = ScheduleFileMetaDataDTO.builder();

        mapBaseFieldsToDTO(entity, builder);   //  reuse parent

        return builder.build();
    }

}