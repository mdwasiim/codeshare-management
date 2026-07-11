package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleStatusDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.ScheduleStatus;
import com.codeshare.airline.master.schedule.mappers.ScheduleStatusMapper;
import com.codeshare.airline.master.schedule.repository.ScheduleStatusRepository;
import com.codeshare.airline.master.schedule.service.ScheduleStatusService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleStatusServiceImpl
        extends BaseServiceImpl<ScheduleStatus, ScheduleStatusDTO, UUID>
        implements ScheduleStatusService {

    public ScheduleStatusServiceImpl(ScheduleStatusRepository repository,
                                     ScheduleStatusMapper mapper) {
        super(repository, mapper);
    }
}
