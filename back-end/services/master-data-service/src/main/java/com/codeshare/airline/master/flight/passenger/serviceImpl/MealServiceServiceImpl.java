package com.codeshare.airline.master.flight.passenger.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.MealServiceDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.passenger.entities.MealService;
import com.codeshare.airline.master.flight.passenger.mappers.MealServiceMapper;
import com.codeshare.airline.master.flight.passenger.repository.MealServiceRepository;
import com.codeshare.airline.master.flight.passenger.service.MealServiceService;
import org.springframework.stereotype.Service;


@Service
public class MealServiceServiceImpl extends BaseServiceImpl<MealService, MealServiceDTO, Long> implements MealServiceService {
    public MealServiceServiceImpl(MealServiceRepository repository, MealServiceMapper mapper) {
        super(repository, mapper);
    }
}