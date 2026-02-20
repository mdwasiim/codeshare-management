package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.SeasonDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.Season;
import org.mapstruct.Mapper;

@Mapper(
        config = CSMMapperConfig.class
)
public interface SeasonMapper extends CSMGenericMapper<Season, SeasonDTO> {

}
