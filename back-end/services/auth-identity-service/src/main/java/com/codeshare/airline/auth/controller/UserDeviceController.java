package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.AuthUserDeviceService;
import com.codeshare.airline.auth.service.AuthUserService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.dto.auth.AuthUserDeviceDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class UserDeviceController {

    private final AuthUserDeviceService AuthUserDeviceService;
    private final AuthUserService AuthUserService;

    // -------------------------------------------------------------------
    // GET ALL DEVICES FOR LOGGED-IN USER
    // -------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<CSMServiceResponse> getDevices(Authentication authentication) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                CSMServiceResponse.success(AuthUserDeviceService.getDevicesByUserId(user.getId()))
        );
    }

    // -------------------------------------------------------------------
    // REGISTER OR UPDATE DEVICE DURING LOGIN
    // -------------------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<CSMServiceResponse> registerDevice(
            @RequestBody AuthUserDeviceDTO authUserDeviceDTO,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                CSMServiceResponse.success(AuthUserDeviceService.registerDevice(user, authUserDeviceDTO))
        );
    }

    // -------------------------------------------------------------------
    // TRUST OR UNTRUST A DEVICE
    // -------------------------------------------------------------------
    @PatchMapping("/{deviceId}/trust")
    public ResponseEntity<CSMServiceResponse> trustDevice(
            @PathVariable String deviceId,
            @RequestParam boolean trusted,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                CSMServiceResponse.success(AuthUserDeviceService.updateTrust(user, deviceId, trusted))
        );
    }

    // -------------------------------------------------------------------
    // DELETE DEVICE + REVOKE ITS REFRESH TOKENS
    // -------------------------------------------------------------------
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<CSMServiceResponse> deleteDevice(
            @PathVariable String deviceId,
            Authentication authentication
    ) {
        AuthUserDTO user = AuthUserService.getByUsername(authentication.getName());
        AuthUserDeviceService.deleteDevice(user, deviceId);

        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }
}
