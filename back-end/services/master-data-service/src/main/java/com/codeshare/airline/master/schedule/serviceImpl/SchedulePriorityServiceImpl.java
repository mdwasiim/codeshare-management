package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.SchedulePriorityDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.SchedulePriority;
import com.codeshare.airline.master.schedule.mappers.SchedulePriorityMapper;
import com.codeshare.airline.master.schedule.repository.SchedulePriorityRepository;
import com.codeshare.airline.master.schedule.service.SchedulePriorityService;
import org.springframework.stereotype.Service;


@Service
public class SchedulePriorityServiceImpl
        extends BaseServiceImpl<SchedulePriority, SchedulePriorityDTO, Long>
        implements SchedulePriorityService {

    public SchedulePriorityServiceImpl(SchedulePriorityRepository repository,
                                       SchedulePriorityMapper mapper) {
        super(repository, mapper);
    }
}
