package com.codeshare.airline.data.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.data.messaging.eitities.DistributionChannel;
import com.codeshare.airline.data.messaging.repository.DistributionChannelRepository;
import com.codeshare.airline.data.messaging.service.DistributionChannelService;
import com.codeshare.airline.data.messaging.utils.mappers.DistributionChannelMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DistributionChannelServiceImpl
        extends BaseServiceImpl<DistributionChannel, DistributionChannelDTO, UUID>
        implements DistributionChannelService {

    public DistributionChannelServiceImpl(DistributionChannelRepository repository,
                                          DistributionChannelMapper mapper) {
        super(repository, mapper);
    }
}
