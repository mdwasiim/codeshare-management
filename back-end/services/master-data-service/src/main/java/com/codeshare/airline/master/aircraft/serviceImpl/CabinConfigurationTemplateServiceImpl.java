package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.CabinConfigurationTemplateDTO;
import com.codeshare.airline.master.aircraft.entities.CabinConfigurationTemplate;
import com.codeshare.airline.master.aircraft.mappers.CabinConfigurationTemplateMapper;
import com.codeshare.airline.master.aircraft.repository.CabinConfigurationTemplateRepository;
import com.codeshare.airline.master.aircraft.service.CabinConfigurationTemplateService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CabinConfigurationTemplateServiceImpl
        extends BaseServiceImpl<CabinConfigurationTemplate, CabinConfigurationTemplateDTO, UUID>
        implements CabinConfigurationTemplateService {

    public CabinConfigurationTemplateServiceImpl(
            CabinConfigurationTemplateRepository repository,
            CabinConfigurationTemplateMapper mapper) {

        super(repository, mapper);
    }
}
