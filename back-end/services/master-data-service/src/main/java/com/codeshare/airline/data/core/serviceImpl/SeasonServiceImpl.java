package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.SeasonDTO;
import com.codeshare.airline.data.core.eitities.Season;
import com.codeshare.airline.data.core.repository.SeasonRepository;
import com.codeshare.airline.data.core.service.SeasonService;
import com.codeshare.airline.data.core.utils.mappers.SeasonMapper;
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