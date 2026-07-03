package com.codeshare.airline.master.flight.passenger.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ReservationBookingDesignatorDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingDesignator;
import com.codeshare.airline.master.flight.passenger.mappers.ReservationBookingDesignatorMapper;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingDesignatorRepository;
import com.codeshare.airline.master.flight.passenger.service.ReservationBookingDesignatorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationBookingDesignatorServiceImpl extends BaseServiceImpl<ReservationBookingDesignator, ReservationBookingDesignatorDTO, UUID> implements ReservationBookingDesignatorService {
    public ReservationBookingDesignatorServiceImpl(ReservationBookingDesignatorRepository repository, ReservationBookingDesignatorMapper mapper) {
        super(repository, mapper);
    }
}