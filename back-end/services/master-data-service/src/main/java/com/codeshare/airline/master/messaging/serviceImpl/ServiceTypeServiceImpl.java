package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ServiceType;
import com.codeshare.airline.master.messaging.repository.ServiceTypeRepository;
import com.codeshare.airline.master.messaging.service.ServiceTypeService;
import com.codeshare.airline.master.messaging.mappers.ServiceTypeMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceTypeServiceImpl
        extends BaseServiceImpl<ServiceType, ServiceTypeDTO, UUID>
        implements ServiceTypeService {

    public ServiceTypeServiceImpl(ServiceTypeRepository repository,
                                  ServiceTypeMapper mapper) {
        super(repository, mapper);
    }
}