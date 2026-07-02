package com.codeshare.airline.master.flightcommercial.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.SecureFlightIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.passenger.entities.SecureFlightIndicator;
import com.codeshare.airline.master.flightcommercial.passenger.mappers.SecureFlightIndicatorMapper;
import com.codeshare.airline.master.flightcommercial.passenger.repository.SecureFlightIndicatorRepository;
import com.codeshare.airline.master.flightcommercial.passenger.service.SecureFlightIndicatorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecureFlightIndicatorServiceImpl extends BaseServiceImpl<SecureFlightIndicator, SecureFlightIndicatorDTO, UUID> implements SecureFlightIndicatorService {
    public SecureFlightIndicatorServiceImpl(SecureFlightIndicatorRepository repository, SecureFlightIndicatorMapper mapper) {
        super(repository, mapper);
    }
}