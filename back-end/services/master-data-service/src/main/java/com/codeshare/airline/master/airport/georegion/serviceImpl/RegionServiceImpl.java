package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.dto.airport.georegion.RegionDTO;
import com.codeshare.airline.master.airport.georegion.eitities.Region;
import com.codeshare.airline.master.airport.georegion.repository.RegionRepository;
import com.codeshare.airline.master.airport.georegion.service.RegionService;
import com.codeshare.airline.master.airport.georegion.utils.mappers.RegionMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region, RegionDTO, UUID>  implements RegionService {

    public RegionServiceImpl(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

}