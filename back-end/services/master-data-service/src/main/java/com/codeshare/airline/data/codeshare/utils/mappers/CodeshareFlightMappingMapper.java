package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareFlightMappingMapper
        extends CSMGenericMapper<CodeshareFlightMapping, CodeshareFlightMappingDTO> {
}