package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.ScheduleType;
import com.codeshare.airline.master.schedule.mappers.ScheduleTypeMapper;
import com.codeshare.airline.master.schedule.repository.ScheduleTypeRepository;
import com.codeshare.airline.master.schedule.service.ScheduleTypeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleTypeServiceImpl
        extends BaseServiceImpl<ScheduleType, ScheduleTypeDTO, UUID>
        implements ScheduleTypeService {

    public ScheduleTypeServiceImpl(ScheduleTypeRepository repository,
                                   ScheduleTypeMapper mapper) {
        super(repository, mapper);
    }
}
