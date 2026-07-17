package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.georegion.SeasonDTO;
import com.codeshare.airline.master.schedule.entities.Season;
import com.codeshare.airline.master.schedule.repository.SeasonRepository;
import com.codeshare.airline.master.schedule.service.SeasonService;
import com.codeshare.airline.master.schedule.mappers.SeasonMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class SeasonServiceImpl extends BaseServiceImpl<Season, SeasonDTO, Long>
        implements SeasonService {

    public SeasonServiceImpl(SeasonRepository repository, SeasonMapper mapper) {
        super(repository, mapper);
    }
}