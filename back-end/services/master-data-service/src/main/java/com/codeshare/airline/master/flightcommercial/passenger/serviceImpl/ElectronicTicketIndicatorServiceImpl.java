package com.codeshare.airline.master.flightcommercial.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ElectronicTicketIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ElectronicTicketIndicator;
import com.codeshare.airline.master.flightcommercial.passenger.mappers.ElectronicTicketIndicatorMapper;
import com.codeshare.airline.master.flightcommercial.passenger.repository.ElectronicTicketIndicatorRepository;
import com.codeshare.airline.master.flightcommercial.passenger.service.ElectronicTicketIndicatorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ElectronicTicketIndicatorServiceImpl extends BaseServiceImpl<ElectronicTicketIndicator, ElectronicTicketIndicatorDTO, UUID> implements ElectronicTicketIndicatorService {
    public ElectronicTicketIndicatorServiceImpl(ElectronicTicketIndicatorRepository repository, ElectronicTicketIndicatorMapper mapper) {
        super(repository, mapper);
    }
}