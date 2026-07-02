package com.codeshare.airline.master.flightcommercial.schedule.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TrafficRestrictionCode;
import com.codeshare.airline.master.flightcommercial.schedule.mappers.TrafficRestrictionCodeMapper;
import com.codeshare.airline.master.flightcommercial.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flightcommercial.schedule.service.TrafficRestrictionCodeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrafficRestrictionCodeServiceImpl extends BaseServiceImpl<TrafficRestrictionCode, TrafficRestrictionCodeDTO, UUID> implements TrafficRestrictionCodeService {
    public TrafficRestrictionCodeServiceImpl(TrafficRestrictionCodeRepository repository, TrafficRestrictionCodeMapper mapper) {
        super(repository, mapper);
    }
}