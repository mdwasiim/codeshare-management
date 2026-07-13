package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import com.codeshare.airline.schedule.live.domain.repository.LiveFlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ActiveScheduleQueryService {

    private final LiveFlightRepository liveFlightRepository;
    private final ActiveScheduleMapper activeScheduleMapper;

    public ActiveScheduleQueryService(
            LiveFlightRepository liveFlightRepository,
            ActiveScheduleMapper activeScheduleMapper
    ) {
        this.liveFlightRepository = liveFlightRepository;
        this.activeScheduleMapper = activeScheduleMapper;
    }

    public ActiveScheduleDTO getActiveSchedule(String airlineCode) {
        return activeScheduleMapper.toActiveSchedule(
                airlineCode,
                liveFlightRepository.findByAirlineCode(airlineCode)
        );
    }
}

