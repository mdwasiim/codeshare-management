package com.codeshare.airline.master.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.SeasonDTO;
import com.codeshare.airline.master.georegion.eitities.Season;
import com.codeshare.airline.master.georegion.repository.SeasonRepository;
import com.codeshare.airline.master.georegion.service.SeasonService;
import com.codeshare.airline.master.georegion.mappers.SeasonMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SeasonServiceImpl extends BaseServiceImpl<Season, SeasonDTO, UUID>
        implements SeasonService {

    public SeasonServiceImpl(SeasonRepository repository, SeasonMapper mapper) {
        super(repository, mapper);
    }
}