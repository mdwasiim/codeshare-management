package com.codeshare.airline.tenant.conbtroller;

import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TenantController {


    private TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/create")
    public ResponseEntity<TenantDTO> login(@RequestBody TenantDTO tenantDTO){

        TenantDTO reponse =  tenantService.createTenant(tenantDTO);

        return  ResponseEntity.ok(reponse);
    }

}
