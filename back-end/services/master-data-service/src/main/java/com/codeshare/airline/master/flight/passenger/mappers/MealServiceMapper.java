package com.codeshare.airline.master.flight.passenger.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.MealServiceDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.passenger.entities.MealService;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface MealServiceMapper extends CSMGenericMapper<MealService, MealServiceDTO> {
}
