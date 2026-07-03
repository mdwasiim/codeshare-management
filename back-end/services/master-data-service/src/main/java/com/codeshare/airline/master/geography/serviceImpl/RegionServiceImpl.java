package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.core.dto.master.georegion.RegionDTO;
import com.codeshare.airline.master.geography.entities.Region;
import com.codeshare.airline.master.geography.repository.RegionRepository;
import com.codeshare.airline.master.geography.service.RegionService;
import com.codeshare.airline.master.geography.mappers.RegionMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region, RegionDTO, UUID>  implements RegionService {

    public RegionServiceImpl(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

}