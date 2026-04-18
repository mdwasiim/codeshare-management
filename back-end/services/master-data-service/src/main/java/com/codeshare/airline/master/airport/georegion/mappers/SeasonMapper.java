package com.codeshare.airline.master.airport.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.SeasonDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.Season;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface SeasonMapper extends CSMGenericMapper<Season, SeasonDTO> {

}
