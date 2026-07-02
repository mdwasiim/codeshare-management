package com.codeshare.airline.master.flightcommercial.schedule.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TimeModeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TimeMode;
import com.codeshare.airline.master.flightcommercial.schedule.mappers.TimeModeMapper;
import com.codeshare.airline.master.flightcommercial.schedule.repository.TimeModeRepository;
import com.codeshare.airline.master.flightcommercial.schedule.service.TimeModeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TimeModeServiceImpl extends BaseServiceImpl<TimeMode, TimeModeDTO, UUID> implements TimeModeService {
    public TimeModeServiceImpl(TimeModeRepository repository, TimeModeMapper mapper) {
        super(repository, mapper);
    }
}