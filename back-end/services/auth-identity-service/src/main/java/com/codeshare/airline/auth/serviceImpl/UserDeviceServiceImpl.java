package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.authentication.RefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserDevice;
import com.codeshare.airline.auth.repository.RefreshTokenRepository;
import com.codeshare.airline.auth.repository.UserDeviceRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.UserDeviceService;
import com.codeshare.airline.auth.utils.mappers.UserDeviceMapper;
import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.auth.identity.model.UserDeviceDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDeviceMapper mapper;
    private final UserRepository userRepository;

    public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository,
                                 RefreshTokenRepository refreshTokenRepository,
                                 UserDeviceMapper mapper,
                                 UserRepository userRepository) {

        this.userDeviceRepository = userDeviceRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    // ----------------------------------------------------------------------
    // Create new device entry (only used internally)
    // ----------------------------------------------------------------------
    @Override
    public UserDeviceDTO create(UserDeviceDTO dto) {
        UserDevice entity = mapper.toEntity(dto);
        return mapper.toDTO(userDeviceRepository.save(entity));
    }


    // ----------------------------------------------------------------------
    // Update device record (e.g., lastSeen, userAgent)
    // ----------------------------------------------------------------------
    @Override
    public UserDeviceDTO update(UUID id, UserDeviceDTO dto) {

        UserDevice entity = userDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Update fields
        entity.setDeviceId(dto.getDeviceId());
        entity.setUserAgent(dto.getUserAgent());
        entity.setLastSeen(LocalDateTime.now());

        return mapper.toDTO(userDeviceRepository.save(entity));
    }


    // ----------------------------------------------------------------------
    // Retrieve all devices associated with a user
    // ----------------------------------------------------------------------
    @Override
    public List<UserDeviceDTO> getDevicesByUserId(UUID userId) {
        List<UserDevice> devices = userDeviceRepository.findByUserId(userId);
        return mapper.toDTOList(devices);
    }


    // ----------------------------------------------------------------------
    // Registers a new device or updates an existing one
    // ----------------------------------------------------------------------
    @Override
    public UserDeviceDTO registerDevice(UserDTO userDTO, UserDeviceDTO dto) {

        Optional<UserDevice> optional = userDeviceRepository
                .findByUserIdAndDeviceId(userDTO.getId(), dto.getDeviceId());

        UserDevice device;

        if (optional.isEmpty()) {
            // Create new device
            device = mapper.toEntity(dto);
            User user = userRepository.getReferenceById(userDTO.getId());
            device.setUser(user);                // attach to user
            device.setLastSeen(LocalDateTime.now());
        } else {
            // Update existing device
            device = optional.get();
            device.setLastSeen(LocalDateTime.now());
            device.setUserAgent(dto.getUserAgent());
        }

        return mapper.toDTO(userDeviceRepository.save(device));
    }


    // ----------------------------------------------------------------------
    // Mark a device as trusted/untrusted
    // ----------------------------------------------------------------------
    @Override
    public UserDeviceDTO updateTrust(UserDTO user, String deviceId, boolean trusted) {

        UserDevice device = userDeviceRepository
                .findByUserIdAndDeviceId(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setTrusted(trusted);
        return mapper.toDTO(userDeviceRepository.save(device));
    }


    // ----------------------------------------------------------------------
    // Delete a device + revoke all refresh tokens for that device
    // ----------------------------------------------------------------------
    @Override
    public void deleteDevice(UserDTO user, String deviceId) {

        UserDevice device = userDeviceRepository
                .findByUserIdAndDeviceId(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Revoke all related refresh tokens
        List<RefreshToken> tokens = refreshTokenRepository.findByUserDevice(device);
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);

        userDeviceRepository.delete(device);
    }


    // ----------------------------------------------------------------------
    // Find a device or create it if missing (used during login)
    // ----------------------------------------------------------------------
    @Override
    public UserDeviceDTO findOrRegisterDevice(UUID userId, UUID tenantId, String deviceId) {

        Optional<UserDevice> optional = userDeviceRepository
                .findByUserIdAndDeviceId(userId, deviceId);

        if (optional.isPresent()) {
            // update last-seen timestamp
            UserDevice existing = optional.get();
            existing.setLastSeen(LocalDateTime.now());
            return mapper.toDTO(userDeviceRepository.save(existing));
        }

        // Create new device
        User user = userRepository.getReferenceById(userId);

        UserDevice newDevice = UserDevice.builder()
                .deviceId(deviceId)
                .tenantId(tenantId)
                .user(user)
                .lastSeen(LocalDateTime.now())
                .trusted(false)
                .build();

        return mapper.toDTO(userDeviceRepository.save(newDevice));
    }
}
