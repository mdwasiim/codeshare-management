package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.TimezoneDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.Timezone;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface TimezoneMapper
        extends CSMGenericMapper<Timezone, TimezoneDTO> {

}