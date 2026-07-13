package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.MealServiceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/meal-services")
public class MealServiceController extends BaseController<MealServiceDTO, Long> {
    protected MealServiceController(BaseService<MealServiceDTO, Long> service) {
        super(service);
    }
}