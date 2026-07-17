package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingDesignator;


public interface ReservationBookingDesignatorRepository extends CSMDataBaseRepository<ReservationBookingDesignator, Long> {
    boolean existsByBookingDesignator(String bookingDesignator);
}
