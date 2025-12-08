package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.UserDeviceService;
import com.codeshare.airline.auth.service.UserService;
import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.auth.identity.model.UserDeviceDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceService userDeviceService;
    private final UserService userService;

    // -------------------------------------------------------------------
    // GET ALL DEVICES FOR LOGGED-IN USER
    // -------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse> getDevices(Authentication authentication) {
        UserDTO user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                ServiceResponse.success(userDeviceService.getDevicesByUserId(user.getId()))
        );
    }

    // -------------------------------------------------------------------
    // REGISTER OR UPDATE DEVICE DURING LOGIN
    // -------------------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> registerDevice(
            @RequestBody UserDeviceDTO userDeviceDTO,
            Authentication authentication
    ) {
        UserDTO user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                ServiceResponse.success(userDeviceService.registerDevice(user, userDeviceDTO))
        );
    }

    // -------------------------------------------------------------------
    // TRUST OR UNTRUST A DEVICE
    // -------------------------------------------------------------------
    @PatchMapping("/{deviceId}/trust")
    public ResponseEntity<ServiceResponse> trustDevice(
            @PathVariable String deviceId,
            @RequestParam boolean trusted,
            Authentication authentication
    ) {
        UserDTO user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                ServiceResponse.success(userDeviceService.updateTrust(user, deviceId, trusted))
        );
    }

    // -------------------------------------------------------------------
    // DELETE DEVICE + REVOKE ITS REFRESH TOKENS
    // -------------------------------------------------------------------
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<ServiceResponse> deleteDevice(
            @PathVariable String deviceId,
            Authentication authentication
    ) {
        UserDTO user = userService.getByUsername(authentication.getName());
        userDeviceService.deleteDevice(user, deviceId);

        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
