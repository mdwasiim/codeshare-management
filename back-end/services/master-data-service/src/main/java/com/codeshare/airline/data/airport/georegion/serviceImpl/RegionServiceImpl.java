package com.codeshare.airline.data.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.RegionDTO;
import com.codeshare.airline.data.airport.georegion.eitities.Region;
import com.codeshare.airline.data.airport.georegion.repository.RegionRepository;
import com.codeshare.airline.data.airport.georegion.service.RegionService;
import com.codeshare.airline.data.airport.georegion.utils.mappers.RegionMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region, RegionDTO, UUID>  implements RegionService {

    public RegionServiceImpl(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

}