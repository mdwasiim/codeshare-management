package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.DeiRegistry;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DeiMapper
        extends CSMGenericMapper<DeiRegistry, DeiDTO> {
}