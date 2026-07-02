package com.codeshare.airline.master.schedule.mappers;

import com.codeshare.airline.core.dto.master.schedule.ScheduleTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.schedule.entities.ScheduleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface ScheduleTypeMapper extends CSMGenericMapper<ScheduleType, ScheduleTypeDTO> {

    @Mapping(source = "scheduleTypeCode", target = "messageTypeCode")
    @Mapping(source = "scheduleTypeName", target = "messageTypeName")
    ScheduleTypeDTO toDTO(ScheduleType entity);

    @Mapping(source = "messageTypeCode", target = "scheduleTypeCode")
    @Mapping(source = "messageTypeName", target = "scheduleTypeName")
    ScheduleType toEntity(ScheduleTypeDTO dto);

    @Mapping(source = "messageTypeCode", target = "scheduleTypeCode")
    @Mapping(source = "messageTypeName", target = "scheduleTypeName")
    void updateEntityFromDto(ScheduleTypeDTO dto, @MappingTarget ScheduleType entity);
}
