package com.codeshare.airline.master.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.RegionDTO;
import com.codeshare.airline.master.georegion.eitities.Region;
import com.codeshare.airline.master.georegion.repository.RegionRepository;
import com.codeshare.airline.master.georegion.service.RegionService;
import com.codeshare.airline.master.georegion.mappers.RegionMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region, RegionDTO, UUID>  implements RegionService {

    public RegionServiceImpl(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

}