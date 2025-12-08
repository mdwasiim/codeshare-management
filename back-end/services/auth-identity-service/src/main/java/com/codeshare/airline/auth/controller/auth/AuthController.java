package com.codeshare.airline.auth.controller.auth;

import com.codeshare.airline.auth.security.JwtUtil;
import com.codeshare.airline.auth.service.Authservice;
import com.codeshare.airline.common.auth.identity.model.AuthRequest;
import com.codeshare.airline.common.auth.identity.model.AuthResponse;
import com.codeshare.airline.common.auth.identity.model.RefreshTokenRequest;
import com.codeshare.airline.common.services.response.ServiceError;
import com.codeshare.airline.common.services.response.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.codeshare.airline.common.services.constant.AppConstan.NO_DATA;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final Authservice authservice;

    // ---------------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<ServiceResponse> login(
            @RequestBody AuthRequest request,
            HttpServletRequest httpReq
    ) {
        try {
            String deviceId = (String) httpReq.getAttribute("X_DEVICE_ID");
            String ua       = (String) httpReq.getAttribute("X_USER_AGENT");
            String ip       = (String) httpReq.getAttribute("X_IP_ADDRESS");

            // Authenticate user
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Generate Token with device tracking
            AuthResponse response = authservice.getLoginToken(
                    authentication.getName(),
                    deviceId, ua, ip
            );

            return ResponseEntity.ok(ServiceResponse.success(response));

        } catch (BadCredentialsException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ServiceResponse.error(ServiceError.builder()
                            .code("401")
                            .message("Invalid username or password")
                            .build()));
        }
    }

    // ---------------------------------------------------------------
    // REFRESH TOKEN
    // ---------------------------------------------------------------
    @PostMapping("/refresh")
    public ResponseEntity<ServiceResponse> refresh(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpReq
    ) {
        String deviceId = (String) httpReq.getAttribute("X_DEVICE_ID");
        String ua       = (String) httpReq.getAttribute("X_USER_AGENT");
        String ip       = (String) httpReq.getAttribute("X_IP_ADDRESS");

        AuthResponse response = authservice.getTokenFromRefreshToken(
                request.getRefreshToken(),
                deviceId, ua, ip
        );

        return ResponseEntity.ok(ServiceResponse.success(response));
    }

    // ---------------------------------------------------------------
    // LOGOUT (Invalidate refresh token + device)
    // ---------------------------------------------------------------
    @PostMapping("/logout")
    public ResponseEntity<ServiceResponse> logout(@RequestBody Map<String, String> req) {

        authservice.logout(req.get("refreshToken"));

        return ResponseEntity.ok(ServiceResponse.success(NO_DATA));
    }

}
