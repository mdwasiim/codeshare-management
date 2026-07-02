package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.master.georegion.SeasonDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.entities.Season;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface SeasonMapper extends CSMGenericMapper<Season, SeasonDTO> {

}
