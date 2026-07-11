package com.codeshare.airline.master.schedule.mappers;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleSourceDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.schedule.entities.ScheduleSource;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ScheduleSourceMapper extends CSMGenericMapper<ScheduleSource, ScheduleSourceDTO> {
}
