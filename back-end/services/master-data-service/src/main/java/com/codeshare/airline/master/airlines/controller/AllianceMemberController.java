package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.core.dto.master.airline.AllianceMemberDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/alliance-members")
public class AllianceMemberController extends BaseController<AllianceMemberDTO, UUID> {
    protected AllianceMemberController(BaseService<AllianceMemberDTO, UUID> service) {
        super(service);
    }
}
