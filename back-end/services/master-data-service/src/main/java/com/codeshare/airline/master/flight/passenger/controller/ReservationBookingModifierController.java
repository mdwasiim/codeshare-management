package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ReservationBookingModifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reservation-booking-modifiers")
public class ReservationBookingModifierController extends BaseController<ReservationBookingModifierDTO, Long> {
    protected ReservationBookingModifierController(BaseService<ReservationBookingModifierDTO, Long> service) {
        super(service);
    }
}