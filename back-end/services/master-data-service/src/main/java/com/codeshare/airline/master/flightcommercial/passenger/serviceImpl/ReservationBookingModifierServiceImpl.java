package com.codeshare.airline.master.flightcommercial.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ReservationBookingModifierDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ReservationBookingModifier;
import com.codeshare.airline.master.flightcommercial.passenger.mappers.ReservationBookingModifierMapper;
import com.codeshare.airline.master.flightcommercial.passenger.repository.ReservationBookingModifierRepository;
import com.codeshare.airline.master.flightcommercial.passenger.service.ReservationBookingModifierService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationBookingModifierServiceImpl extends BaseServiceImpl<ReservationBookingModifier, ReservationBookingModifierDTO, UUID> implements ReservationBookingModifierService {
    public ReservationBookingModifierServiceImpl(ReservationBookingModifierRepository repository, ReservationBookingModifierMapper mapper) {
        super(repository, mapper);
    }
}