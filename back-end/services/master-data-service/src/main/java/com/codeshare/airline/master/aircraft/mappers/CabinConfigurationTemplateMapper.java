package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.platform.core.dto.master.aircraft.CabinConfigurationTemplateDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.CabinConfigurationTemplate;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface CabinConfigurationTemplateMapper
        extends CSMGenericMapper<CabinConfigurationTemplate, CabinConfigurationTemplateDTO> {
}
