package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AllianceMemberDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/alliance-members")
public class AllianceMemberController extends BaseController<AllianceMemberDTO, Long> {
    protected AllianceMemberController(BaseService<AllianceMemberDTO, Long> service) {
        super(service);
    }
}
