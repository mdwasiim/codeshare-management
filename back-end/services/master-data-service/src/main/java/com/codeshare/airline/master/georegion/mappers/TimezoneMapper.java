package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.TimezoneDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.Timezone;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TimezoneMapper
        extends CSMGenericMapper<Timezone, TimezoneDTO> {

}