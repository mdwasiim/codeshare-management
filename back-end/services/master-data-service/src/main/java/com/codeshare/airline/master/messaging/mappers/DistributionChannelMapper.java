package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.platform.core.dto.master.messaging.DistributionChannelDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.DistributionChannel;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DistributionChannelMapper
        extends CSMGenericMapper<DistributionChannel, DistributionChannelDTO> {
}
