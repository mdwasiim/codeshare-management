package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.ServiceTypeDTO;
import com.codeshare.airline.data.core.eitities.ServiceType;
import com.codeshare.airline.data.core.repository.ServiceTypeRepository;
import com.codeshare.airline.data.core.service.ServiceTypeService;
import com.codeshare.airline.data.core.utils.mappers.ServiceTypeMapper;
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