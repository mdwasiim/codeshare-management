package com.codeshare.airline.data.messaging.utils.mappers;

import com.codeshare.airline.core.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.messaging.eitities.DistributionChannel;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DistributionChannelMapper
        extends CSMGenericMapper<DistributionChannel, DistributionChannelDTO> {
}
