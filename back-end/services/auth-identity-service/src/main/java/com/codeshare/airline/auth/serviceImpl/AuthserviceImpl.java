package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.authentication.RefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserDevice;
import com.codeshare.airline.auth.repository.RefreshTokenRepository;
import com.codeshare.airline.auth.repository.UserDeviceRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.security.JwtUtil;
import com.codeshare.airline.auth.service.Authservice;
import com.codeshare.airline.auth.service.RolePermissionResolverService;
import com.codeshare.airline.auth.service.UserDeviceService;
import com.codeshare.airline.auth.utils.mappers.UserDeviceMapper;
import com.codeshare.airline.auth.utils.mappers.UserMapper;
import com.codeshare.airline.common.auth.identity.model.AuthResponse;
import com.codeshare.airline.common.auth.identity.model.UserDeviceDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthserviceImpl implements Authservice {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserDeviceService userDeviceService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserDeviceMapper userDeviceMapper;
    private final RolePermissionResolverService rolePermissionResolverService;

    public AuthserviceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           UserDeviceRepository userDeviceRepository,
                           PasswordEncoder passwordEncoder,
                           UserDeviceService userDeviceService,
                           JwtUtil jwtUtil,
                           UserMapper userMapper,
                           UserDeviceMapper userDeviceMapper,
                           RolePermissionResolverService rolePermissionResolverService) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDeviceService = userDeviceService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.userDeviceMapper = userDeviceMapper;
        this.rolePermissionResolverService = rolePermissionResolverService;
    }


    // ====================================================================================
    // LOGIN: Create Access Token + Refresh Token
    // ====================================================================================
    @Override
    public AuthResponse getLoginToken(String userName, String deviceId, String userAgent, String ip) {

        // 1️⃣ Fetch user (multi-tenant safe)
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        UUID tenantId = user.getTenantId();

        // 2️⃣ Find or create device entry
        UserDevice device = userDeviceRepository
                .findByUserIdAndDeviceId(user.getId(), deviceId)
                .orElseGet(() -> {
                    UserDeviceDTO dto = UserDeviceDTO.builder()
                            .deviceId(deviceId)
                            .userAgent(userAgent)
                            .lastSeen(LocalDateTime.now())
                            .active(false)
                            .tenantId(tenantId)
                            .build();
                    dto.setUser(userMapper.toDTO(user));
                    return userDeviceRepository.save(userDeviceMapper.toEntity(dto));
                });

        // Update last-seen info
        device.setLastSeen(LocalDateTime.now());
        device.setUserAgent(userAgent);
        userDeviceRepository.save(device);

        // 3️⃣ Create Access Token (includes device + ip info)
        String accessToken = jwtUtil.generateAccessToken(user, deviceId, userAgent, ip);

        // 4️⃣ Create Refresh Token (raw + hashed)
        String rawRefreshToken = UUID.randomUUID().toString();
        String hashedRefreshToken = passwordEncoder.encode(rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenHash(hashedRefreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .user(user)
                .tenantId(tenantId)
                .userAgent(userAgent)
                .ipAddress(ip)
                .userDevice(device)
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5️⃣ Fetch roles + permissions
        Set<String> roles = rolePermissionResolverService.resolveRoleNames(user.getId(), tenantId);
        Set<String> permissions = rolePermissionResolverService.resolvePermissionsNames(user.getId(), tenantId);

        // 6️⃣ Return raw refresh token to the client
        return new AuthResponse(
                accessToken,
                rawRefreshToken,
                userMapper.toDTO(user),
                roles,
                permissions
        );
    }


    // ====================================================================================
    // REFRESH: Exchange Refresh Token for new Access Token + New Refresh Token
    // ====================================================================================
    @Override
    public AuthResponse getTokenFromRefreshToken(String rawRefreshToken, String deviceId, String userAgent, String ip) {

        // 1️⃣ Find refresh token by scanning all stored hashes
        RefreshToken storedToken = refreshTokenRepository
                .findAll()
                .stream()
                .filter(t -> passwordEncoder.matches(rawRefreshToken, t.getRefreshTokenHash()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // 2️⃣ Validate expiration or revocation
        if (storedToken.isRevoked() || storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        // 3️⃣ Rotate token (optional: add reuse detection here)
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // 4️⃣ Issue new login session
        return getLoginToken(
                storedToken.getUser().getUsername(),
                deviceId,
                userAgent,
                ip
        );
    }


    // ====================================================================================
    // LOGOUT: Revoke Refresh Token
    // ====================================================================================
    @Override
    public void logout(String rawRefreshToken) {

        RefreshToken storedToken = refreshTokenRepository
                .findAll()
                .stream()
                .filter(token -> passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // Revoke instead of deleting (security best practice)
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
    }
}

