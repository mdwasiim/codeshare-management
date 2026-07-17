package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ReservationBookingDesignatorDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reservation-booking-designators")
public class ReservationBookingDesignatorController extends BaseController<ReservationBookingDesignatorDTO, Long> {
    protected ReservationBookingDesignatorController(BaseService<ReservationBookingDesignatorDTO, Long> service) {
        super(service);
    }
}