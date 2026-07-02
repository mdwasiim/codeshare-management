package com.codeshare.airline.master.flightcommercial.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.MealServiceDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.passenger.entities.MealService;
import com.codeshare.airline.master.flightcommercial.passenger.mappers.MealServiceMapper;
import com.codeshare.airline.master.flightcommercial.passenger.repository.MealServiceRepository;
import com.codeshare.airline.master.flightcommercial.passenger.service.MealServiceService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MealServiceServiceImpl extends BaseServiceImpl<MealService, MealServiceDTO, UUID> implements MealServiceService {
    public MealServiceServiceImpl(MealServiceRepository repository, MealServiceMapper mapper) {
        super(repository, mapper);
    }
}