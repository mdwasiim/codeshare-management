package com.codeshare.airline.master.flight.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.FlightSuffixDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.schedule.entities.FlightSuffix;
import com.codeshare.airline.master.flight.schedule.mappers.FlightSuffixMapper;
import com.codeshare.airline.master.flight.schedule.repository.FlightSuffixRepository;
import com.codeshare.airline.master.flight.schedule.service.FlightSuffixService;
import org.springframework.stereotype.Service;


@Service
public class FlightSuffixServiceImpl extends BaseServiceImpl<FlightSuffix, FlightSuffixDTO, Long> implements FlightSuffixService {
    public FlightSuffixServiceImpl(FlightSuffixRepository repository, FlightSuffixMapper mapper) {
        super(repository, mapper);
    }
}