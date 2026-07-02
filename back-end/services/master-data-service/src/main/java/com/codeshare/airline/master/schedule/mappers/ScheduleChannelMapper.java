package com.codeshare.airline.master.schedule.mappers;

import com.codeshare.airline.core.dto.master.schedule.ScheduleChannelDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.schedule.entities.ScheduleChannel;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ScheduleChannelMapper extends CSMGenericMapper<ScheduleChannel, ScheduleChannelDTO> {
}
