package com.codeshare.airline.master.flight.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ElectronicTicketIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.passenger.entities.ElectronicTicketIndicator;
import com.codeshare.airline.master.flight.passenger.mappers.ElectronicTicketIndicatorMapper;
import com.codeshare.airline.master.flight.passenger.repository.ElectronicTicketIndicatorRepository;
import com.codeshare.airline.master.flight.passenger.service.ElectronicTicketIndicatorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ElectronicTicketIndicatorServiceImpl extends BaseServiceImpl<ElectronicTicketIndicator, ElectronicTicketIndicatorDTO, UUID> implements ElectronicTicketIndicatorService {
    public ElectronicTicketIndicatorServiceImpl(ElectronicTicketIndicatorRepository repository, ElectronicTicketIndicatorMapper mapper) {
        super(repository, mapper);
    }
}