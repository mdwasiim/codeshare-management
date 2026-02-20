package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.RegionDTO;
import com.codeshare.airline.data.core.eitities.Region;
import com.codeshare.airline.data.core.repository.RegionRepository;
import com.codeshare.airline.data.core.service.RegionService;
import com.codeshare.airline.data.core.utils.mappers.RegionMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region, RegionDTO, UUID>  implements RegionService {

    public RegionServiceImpl(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

}