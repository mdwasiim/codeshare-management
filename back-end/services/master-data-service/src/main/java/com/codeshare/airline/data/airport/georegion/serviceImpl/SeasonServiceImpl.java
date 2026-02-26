package com.codeshare.airline.data.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.SeasonDTO;
import com.codeshare.airline.data.airport.georegion.eitities.Season;
import com.codeshare.airline.data.airport.georegion.repository.SeasonRepository;
import com.codeshare.airline.data.airport.georegion.service.SeasonService;
import com.codeshare.airline.data.airport.georegion.utils.mappers.SeasonMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SeasonServiceImpl extends BaseServiceImpl<Season, SeasonDTO, UUID>
        implements SeasonService {

    public SeasonServiceImpl(SeasonRepository repository, SeasonMapper mapper) {
        super(repository, mapper);
    }
}