package com.codeshare.airline.auth.controller.auth;

import com.codeshare.airline.auth.security.JwtUtil;
import com.codeshare.airline.auth.service.Authservice;
import com.codeshare.airline.common.auth.model.AuthRequest;
import com.codeshare.airline.common.auth.model.AuthResponse;
import com.codeshare.airline.common.auth.model.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private Authservice authservice;

    @Autowired
    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, Authservice authservice) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authservice = authservice;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()) );
        return  ResponseEntity.ok(authservice.getLoginToken(request.getUsername()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse > refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();
        return  ResponseEntity.ok(authservice.getTokenFromRefreshToken(requestRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String,String> request){
        String refreshTokenStr = request.get("refreshToken");
        authservice.logout(refreshTokenStr);
        return ResponseEntity.noContent().build();
    }

}
