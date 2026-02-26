package com.codeshare.airline.data.airport.georegion.utils.mappers;

import com.codeshare.airline.core.dto.airport.georegion.TimezoneDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.airport.georegion.eitities.Timezone;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TimezoneMapper
        extends CSMGenericMapper<Timezone, TimezoneDTO> {

}