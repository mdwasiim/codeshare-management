package com.codeshare.airline.master.schedule.mappers;

import com.codeshare.airline.platform.core.dto.master.schedule.OperationalSuffixDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.schedule.entities.OperationalSuffix;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface OperationalSuffixMapper extends CSMGenericMapper<OperationalSuffix, OperationalSuffixDTO> {
}
