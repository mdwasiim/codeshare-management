package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingModifier;


public interface ReservationBookingModifierRepository extends CSMDataBaseRepository<ReservationBookingModifier, Long> {
    boolean existsByModifierCode(String modifierCode);
}
