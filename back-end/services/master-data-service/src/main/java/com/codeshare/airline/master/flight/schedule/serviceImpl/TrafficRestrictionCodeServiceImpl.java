package com.codeshare.airline.master.flight.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;
import com.codeshare.airline.master.flight.schedule.mappers.TrafficRestrictionCodeMapper;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flight.schedule.service.TrafficRestrictionCodeService;
import org.springframework.stereotype.Service;


@Service
public class TrafficRestrictionCodeServiceImpl extends BaseServiceImpl<TrafficRestrictionCode, TrafficRestrictionCodeDTO, Long> implements TrafficRestrictionCodeService {
    public TrafficRestrictionCodeServiceImpl(TrafficRestrictionCodeRepository repository, TrafficRestrictionCodeMapper mapper) {
        super(repository, mapper);
    }
}