package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleCategoryDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedule-categories")
public class ScheduleCategoryController extends BaseController<ScheduleCategoryDTO, Long> {

    protected ScheduleCategoryController(BaseService<ScheduleCategoryDTO, Long> service) {
        super(service);
    }
}
