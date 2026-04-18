package com.codeshare.airline.master.commercial.codeshare.utils.mappers;

import com.codeshare.airline.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareFlightMapping;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareFlightMappingMapper
        extends CSMGenericMapper<CodeshareFlightMapping, CodeshareFlightMappingDTO> {
}