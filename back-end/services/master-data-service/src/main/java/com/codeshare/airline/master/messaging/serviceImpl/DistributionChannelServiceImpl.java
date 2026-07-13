package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.DistributionChannelDTO;
import com.codeshare.airline.master.messaging.entities.DistributionChannel;
import com.codeshare.airline.master.messaging.repository.DistributionChannelRepository;
import com.codeshare.airline.master.messaging.service.DistributionChannelService;
import com.codeshare.airline.master.messaging.mappers.DistributionChannelMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DistributionChannelServiceImpl
        extends BaseServiceImpl<DistributionChannel, DistributionChannelDTO, Long>
        implements DistributionChannelService {

    public DistributionChannelServiceImpl(DistributionChannelRepository repository,
                                          DistributionChannelMapper mapper) {
        super(repository, mapper);
    }
}
