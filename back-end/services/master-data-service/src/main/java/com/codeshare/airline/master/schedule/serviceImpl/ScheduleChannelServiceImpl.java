package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleChannelDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.ScheduleChannel;
import com.codeshare.airline.master.schedule.mappers.ScheduleChannelMapper;
import com.codeshare.airline.master.schedule.repository.ScheduleChannelRepository;
import com.codeshare.airline.master.schedule.service.ScheduleChannelService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleChannelServiceImpl
        extends BaseServiceImpl<ScheduleChannel, ScheduleChannelDTO, UUID>
        implements ScheduleChannelService {

    public ScheduleChannelServiceImpl(ScheduleChannelRepository repository,
                                      ScheduleChannelMapper mapper) {
        super(repository, mapper);
    }
}
