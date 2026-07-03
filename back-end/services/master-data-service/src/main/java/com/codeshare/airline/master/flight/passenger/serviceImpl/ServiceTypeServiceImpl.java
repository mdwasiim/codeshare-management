package com.codeshare.airline.master.flight.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;
import com.codeshare.airline.master.flight.passenger.mappers.ServiceTypeMapper;
import com.codeshare.airline.master.flight.passenger.repository.ServiceTypeRepository;
import com.codeshare.airline.master.flight.passenger.service.ServiceTypeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceTypeServiceImpl extends BaseServiceImpl<ServiceType, ServiceTypeDTO, UUID> implements ServiceTypeService {
    public ServiceTypeServiceImpl(ServiceTypeRepository repository, ServiceTypeMapper mapper) {
        super(repository, mapper);
    }
}