package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingModifier;

import java.util.UUID;

public interface ReservationBookingModifierRepository extends CSMDataBaseRepository<ReservationBookingModifier, UUID> {
    boolean existsByModifierCode(String modifierCode);
}
