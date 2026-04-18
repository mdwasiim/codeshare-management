package com.codeshare.airline.messaging.utils.mappers;

import com.codeshare.airline.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.messaging.eitities.DistributionChannel;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface DistributionChannelMapper
        extends CSMGenericMapper<DistributionChannel, DistributionChannelDTO> {
}
