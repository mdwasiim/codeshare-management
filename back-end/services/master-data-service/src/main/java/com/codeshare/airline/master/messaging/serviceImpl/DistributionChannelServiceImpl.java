package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.master.messaging.eitities.DistributionChannel;
import com.codeshare.airline.master.messaging.repository.DistributionChannelRepository;
import com.codeshare.airline.master.messaging.service.DistributionChannelService;
import com.codeshare.airline.master.messaging.mappers.DistributionChannelMapper;
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
