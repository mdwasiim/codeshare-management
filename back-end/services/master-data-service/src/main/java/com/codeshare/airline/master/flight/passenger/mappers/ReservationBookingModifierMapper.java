package com.codeshare.airline.master.flight.passenger.mappers;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ReservationBookingModifierDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingModifier;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ReservationBookingModifierMapper extends CSMGenericMapper<ReservationBookingModifier, ReservationBookingModifierDTO> {
}
