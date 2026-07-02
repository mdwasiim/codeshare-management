package com.codeshare.airline.master.flightcommercial.passenger.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ReservationBookingDesignatorDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ReservationBookingDesignator;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ReservationBookingDesignatorMapper extends CSMGenericMapper<ReservationBookingDesignator, ReservationBookingDesignatorDTO> {
}
