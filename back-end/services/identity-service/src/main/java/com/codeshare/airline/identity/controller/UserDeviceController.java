package com.codeshare.airline.identity.controller;

import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.dto.auth.UserDeviceDTO;
import com.codeshare.airline.identity.service.AuthUserDeviceService;
import com.codeshare.airline.identity.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class UserDeviceController {

    private final AuthUserDeviceService AuthUserDeviceService;
    private final AuthUserService AuthUserService;

    // -------------------------------------------------------------------
    // GET ALL DEVICES FOR LOGGED-IN USER
    // -------------------------------------------------------------------
    @GetMapping
    public List<UserDeviceDTO> getDevices(Authentication authentication) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return AuthUserDeviceService.getDevicesByUserId(user.getId());
    }

    // -------------------------------------------------------------------
    // REGISTER OR UPDATE DEVICE DURING LOGIN
    // -------------------------------------------------------------------
    @PostMapping
    public UserDeviceDTO  registerDevice(
            @RequestBody UserDeviceDTO authUserDeviceDTO,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return AuthUserDeviceService.registerDevice(user, authUserDeviceDTO);
    }

    // -------------------------------------------------------------------
    // TRUST OR UNTRUST A DEVICE
    // -------------------------------------------------------------------
    @PutMapping("/{deviceId}/trust")
    public UserDeviceDTO  trustDevice(
            @PathVariable String deviceId,
            @RequestParam boolean trusted,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return AuthUserDeviceService.updateTrust(user, deviceId, trusted);
    }

    // -------------------------------------------------------------------
    // DELETE DEVICE + REVOKE ITS REFRESH TOKENS
    // -------------------------------------------------------------------
    @DeleteMapping
    public void   deleteDevice(
            @PathVariable String deviceId,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        AuthUserDeviceService.deleteDevice(user, deviceId);

    }
}
