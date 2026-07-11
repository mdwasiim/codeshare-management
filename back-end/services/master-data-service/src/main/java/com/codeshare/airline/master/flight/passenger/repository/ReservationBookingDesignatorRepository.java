package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingDesignator;

import java.util.UUID;

public interface ReservationBookingDesignatorRepository extends CSMDataBaseRepository<ReservationBookingDesignator, UUID> {
    boolean existsByBookingDesignator(String bookingDesignator);
}
