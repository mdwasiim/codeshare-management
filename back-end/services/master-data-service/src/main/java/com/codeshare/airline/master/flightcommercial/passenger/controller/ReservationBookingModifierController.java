package com.codeshare.airline.master.flightcommercial.passenger.controller;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.ReservationBookingModifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/reservation-booking-modifiers")
public class ReservationBookingModifierController extends BaseController<ReservationBookingModifierDTO, UUID> {
    protected ReservationBookingModifierController(BaseService<ReservationBookingModifierDTO, UUID> service) {
        super(service);
    }
}