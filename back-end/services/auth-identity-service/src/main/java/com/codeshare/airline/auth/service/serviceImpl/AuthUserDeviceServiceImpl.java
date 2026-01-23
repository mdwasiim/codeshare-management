package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.model.entities.User;
import com.codeshare.airline.auth.model.entities.UserDeviceEntity;
import com.codeshare.airline.auth.repository.UserDeviceRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.AuthUserDeviceService;
import com.codeshare.airline.auth.utils.mappers.AuthUserDeviceMapper;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.dto.auth.AuthUserDeviceDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthUserDeviceServiceImpl implements AuthUserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final AuthUserDeviceMapper authUserDeviceMapper;
    private final UserRepository userRepository;

    public AuthUserDeviceServiceImpl(UserDeviceRepository userDeviceRepository,
                                     AuthUserDeviceMapper authUserDeviceMapper,
                                     UserRepository userRepository) {

        this.userDeviceRepository = userDeviceRepository;
        this.authUserDeviceMapper = authUserDeviceMapper;
        this.userRepository = userRepository;
    }


    // ----------------------------------------------------------------------
    // Create new device entry (only used internally)
    // ----------------------------------------------------------------------
    @Override
    public AuthUserDeviceDTO create(AuthUserDeviceDTO dto) {
        UserDeviceEntity entity = authUserDeviceMapper.toEntity(dto);
        return authUserDeviceMapper.toDTO(userDeviceRepository.save(entity));
    }


    // ----------------------------------------------------------------------
    // Update device record (e.g., lastSeen, userAgent)
    // ----------------------------------------------------------------------
    @Override
    public AuthUserDeviceDTO update(UUID id, AuthUserDeviceDTO dto) {

        UserDeviceEntity entity = userDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Update fields
        entity.setDeviceId(dto.getDeviceId());
        entity.setUserAgent(dto.getUserAgent());
        entity.setLastSeen(LocalDateTime.now());

        return authUserDeviceMapper.toDTO(userDeviceRepository.save(entity));
    }


    // ----------------------------------------------------------------------
    // Retrieve all devices associated with a userEntity
    // ----------------------------------------------------------------------
    @Override
    public List<AuthUserDeviceDTO> getDevicesByUserId(UUID userId) {
        List<UserDeviceEntity> devices = userDeviceRepository.findByUser_Id(userId);
        return authUserDeviceMapper.toDTOList(devices);
    }


    // ----------------------------------------------------------------------
    // Registers a new device or updates an existing one
    // ----------------------------------------------------------------------
    @Override
    public AuthUserDeviceDTO registerDevice(AuthUserDTO authUserDTO, AuthUserDeviceDTO dto) {

        Optional<UserDeviceEntity> optional = userDeviceRepository
                .findByUser_IdAndDeviceId(authUserDTO.getId(), dto.getDeviceId());

        UserDeviceEntity device;

        if (optional.isEmpty()) {
            // Create new device
            device = authUserDeviceMapper.toEntity(dto);
            User oauthUser = userRepository.getReferenceById(authUserDTO.getId());
            device.setUser(oauthUser);                // attach to User
            device.setLastSeen(LocalDateTime.now());
        } else {
            // Update existing device
            device = optional.get();
            device.setLastSeen(LocalDateTime.now());
            device.setUserAgent(dto.getUserAgent());
        }

        return authUserDeviceMapper.toDTO(userDeviceRepository.save(device));
    }


    // ----------------------------------------------------------------------
    // Mark a device as trusted/untrusted
    // ----------------------------------------------------------------------
    @Override
    public AuthUserDeviceDTO updateTrust(AuthUserDTO user, String deviceId, boolean trusted) {

        UserDeviceEntity device = userDeviceRepository
                .findByUser_IdAndDeviceId(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setTrusted(trusted);
        return authUserDeviceMapper.toDTO(userDeviceRepository.save(device));
    }


    // ----------------------------------------------------------------------
    // Delete a device + revoke all refresh tokens for that device
    // ----------------------------------------------------------------------
    @Override
    public void deleteDevice(AuthUserDTO user, String deviceId) {

        UserDeviceEntity device = userDeviceRepository
                .findByUser_IdAndDeviceId(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        userDeviceRepository.delete(device);
    }


    // ----------------------------------------------------------------------
    // Find a device or create it if missing (used during login)
    // ----------------------------------------------------------------------
    @Override
    public AuthUserDeviceDTO findOrRegisterDevice(UUID userId, UUID tenantId, String deviceId) {

        Optional<UserDeviceEntity> optional = userDeviceRepository
                .findByUser_IdAndDeviceId(userId, deviceId);

        if (optional.isPresent()) {
            // update last-seen timestamp
            UserDeviceEntity existing = optional.get();
            existing.setLastSeen(LocalDateTime.now());
            return authUserDeviceMapper.toDTO(userDeviceRepository.save(existing));
        }

        // Create new device
        Optional<User> oauthUser = userRepository.findById(userId);

        UserDeviceEntity newDevice = UserDeviceEntity.builder()
                .deviceId(deviceId)
                .user(oauthUser.get())
                .lastSeen(LocalDateTime.now())
                .trusted(false)
                .build();

        return authUserDeviceMapper.toDTO(userDeviceRepository.save(newDevice));
    }

    @Override
    public void track(User user, String deviceId, String userAgent, String ip) {
        UserDeviceEntity device = userDeviceRepository
                .findByUser_IdAndDeviceId(user.getId(), deviceId)
                .orElseGet(() -> {
                    AuthUserDeviceDTO dto = AuthUserDeviceDTO.builder()
                            .deviceId(deviceId)
                            .userAgent(userAgent)
                            .lastSeen(LocalDateTime.now())
                            .active(false)
                            .build();
                    UserDeviceEntity userDeviceEntity = authUserDeviceMapper.toEntity(dto);
                    userDeviceEntity.setUser(user);
                    return userDeviceRepository.save(userDeviceEntity);
                });

        // Update last-seen info
        device.setLastSeen(LocalDateTime.now());
        device.setUserAgent(userAgent);
        userDeviceRepository.save(device);
    }
}
