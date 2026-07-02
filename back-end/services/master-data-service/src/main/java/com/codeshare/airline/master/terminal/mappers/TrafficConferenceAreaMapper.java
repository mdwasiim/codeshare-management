package com.codeshare.airline.master.terminal.mappers;

import com.codeshare.airline.core.dto.master.terminal.TrafficConferenceAreaDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.terminal.entities.TrafficConferenceArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface TrafficConferenceAreaMapper
        extends CSMGenericMapper<TrafficConferenceArea, TrafficConferenceAreaDTO> {

    @Mapping(source = "region.id", target = "regionId")
    @Mapping(source = "region.regionCode", target = "regionCode")
    TrafficConferenceAreaDTO toDTO(TrafficConferenceArea entity);

    @Mapping(target = "region", ignore = true)
    TrafficConferenceArea toEntity(TrafficConferenceAreaDTO dto);

    @Mapping(target = "region", ignore = true)
    void updateEntityFromDto(TrafficConferenceAreaDTO dto, @MappingTarget TrafficConferenceArea entity);
}
