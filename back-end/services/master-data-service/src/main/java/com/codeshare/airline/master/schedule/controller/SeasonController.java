package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.georegion.SeasonDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/seasons")
public class SeasonController extends BaseController<SeasonDTO, Long> {

    protected SeasonController(BaseService<SeasonDTO, Long> service) {
        super(service);
    }
}