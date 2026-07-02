package com.codeshare.airline.master.flightcommercial.passenger.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ReservationBookingModifier;

import java.util.UUID;

public interface ReservationBookingModifierRepository extends CSMDataBaseRepository<ReservationBookingModifier, UUID> {
}
