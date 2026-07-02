package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.mappers.ScheduleTypeMapper;
import com.codeshare.airline.master.messaging.repository.ScheduleTypeRepository;
import com.codeshare.airline.master.messaging.service.ScheduleTypeService;
import com.codeshare.airline.master.schedule.entities.ScheduleType;
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
