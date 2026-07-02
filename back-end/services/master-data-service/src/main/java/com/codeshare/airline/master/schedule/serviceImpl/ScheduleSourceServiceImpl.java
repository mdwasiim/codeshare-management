package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.core.dto.master.schedule.ScheduleSourceDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.ScheduleSource;
import com.codeshare.airline.master.schedule.mappers.ScheduleSourceMapper;
import com.codeshare.airline.master.schedule.repository.ScheduleSourceRepository;
import com.codeshare.airline.master.schedule.service.ScheduleSourceService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleSourceServiceImpl
        extends BaseServiceImpl<ScheduleSource, ScheduleSourceDTO, UUID>
        implements ScheduleSourceService {

    public ScheduleSourceServiceImpl(ScheduleSourceRepository repository,
                                     ScheduleSourceMapper mapper) {
        super(repository, mapper);
    }
}
