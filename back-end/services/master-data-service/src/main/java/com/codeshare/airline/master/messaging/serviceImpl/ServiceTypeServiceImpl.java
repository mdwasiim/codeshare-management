package com.codeshare.airline.messaging.serviceImpl;

import com.codeshare.airline.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.messaging.eitities.ServiceType;
import com.codeshare.airline.messaging.repository.ServiceTypeRepository;
import com.codeshare.airline.messaging.service.ServiceTypeService;
import com.codeshare.airline.messaging.utils.mappers.ServiceTypeMapper;
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