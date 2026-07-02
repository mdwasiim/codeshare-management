package com.codeshare.airline.master.flightcommercial.schedule.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.FlightFrequencyDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.schedule.entities.FlightFrequency;
import com.codeshare.airline.master.flightcommercial.schedule.mappers.FlightFrequencyMapper;
import com.codeshare.airline.master.flightcommercial.schedule.repository.FlightFrequencyRepository;
import com.codeshare.airline.master.flightcommercial.schedule.service.FlightFrequencyService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightFrequencyServiceImpl extends BaseServiceImpl<FlightFrequency, FlightFrequencyDTO, UUID> implements FlightFrequencyService {
    public FlightFrequencyServiceImpl(FlightFrequencyRepository repository, FlightFrequencyMapper mapper) {
        super(repository, mapper);
    }
}