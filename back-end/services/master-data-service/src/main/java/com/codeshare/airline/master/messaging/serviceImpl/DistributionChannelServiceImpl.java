package com.codeshare.airline.messaging.serviceImpl;

import com.codeshare.airline.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.messaging.eitities.DistributionChannel;
import com.codeshare.airline.messaging.repository.DistributionChannelRepository;
import com.codeshare.airline.messaging.service.DistributionChannelService;
import com.codeshare.airline.messaging.utils.mappers.DistributionChannelMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
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
