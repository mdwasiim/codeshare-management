package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.auth.RefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.utils.mappers.UserMapper;
import com.codeshare.airline.auth.repository.RefreshTokenRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.security.JwtUtil;
import com.codeshare.airline.auth.service.Authservice;
import com.codeshare.airline.auth.service.RolePermissionResolverService;
import com.codeshare.airline.common.auth.model.AuthResponse;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthserviceImpl implements Authservice {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    private final RolePermissionResolverService rolePermissionResolverService;

    public AuthserviceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, UserMapper userMapper, RolePermissionResolverService rolePermissionResolverService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.rolePermissionResolverService = rolePermissionResolverService;
    }


    @Override
    public AuthResponse getLoginToken(String userName){

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshTokenStr = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);

        Set<RoleDTO> roles = rolePermissionResolverService.resolveRoleNames(user.getId(), user.getTenantId());

        Set<PermissionDTO> permissions = jwtUtil.getAllUserPermissions(user);

        return new AuthResponse(accessToken, refreshTokenStr, userMapper.toDTO(user),  roles, permissions);
    }

    @Override
    public AuthResponse getTokenFromRefreshToken(String requestRefreshToken){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return getLoginToken(refreshToken.getUser().getUsername());
    }

    @Override
    public void logout(String refreshTokenstr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenstr)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        refreshTokenRepository.delete(refreshToken);
    }


}
