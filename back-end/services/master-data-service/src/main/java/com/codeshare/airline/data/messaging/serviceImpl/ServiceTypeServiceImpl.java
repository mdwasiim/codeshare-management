package com.codeshare.airline.data.messaging.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.data.messaging.eitities.ServiceType;
import com.codeshare.airline.data.messaging.repository.ServiceTypeRepository;
import com.codeshare.airline.data.messaging.service.ServiceTypeService;
import com.codeshare.airline.data.messaging.utils.mappers.ServiceTypeMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
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