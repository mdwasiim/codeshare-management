package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.SeasonDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController extends BaseController<SeasonDTO, UUID> {

    protected SeasonController(BaseService<SeasonDTO, UUID> service) {
        super(service);
    }
}