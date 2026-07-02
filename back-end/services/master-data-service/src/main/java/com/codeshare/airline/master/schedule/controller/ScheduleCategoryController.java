package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.ScheduleCategoryDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-categories")
public class ScheduleCategoryController extends BaseController<ScheduleCategoryDTO, UUID> {

    protected ScheduleCategoryController(BaseService<ScheduleCategoryDTO, UUID> service) {
        super(service);
    }
}
