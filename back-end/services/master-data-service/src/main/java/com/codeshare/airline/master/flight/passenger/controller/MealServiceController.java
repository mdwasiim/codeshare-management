package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.MealServiceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/meal-services")
public class MealServiceController extends BaseController<MealServiceDTO, UUID> {
    protected MealServiceController(BaseService<MealServiceDTO, UUID> service) {
        super(service);
    }
}