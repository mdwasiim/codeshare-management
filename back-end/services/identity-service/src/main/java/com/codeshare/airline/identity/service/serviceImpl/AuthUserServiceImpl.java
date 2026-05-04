package com.codeshare.airline.identity.service.serviceImpl;


import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.identity.authentication.domain.TenantContextHolder;
import com.codeshare.airline.identity.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.identity.repository.UserRepository;
import com.codeshare.airline.identity.service.AuthUserService;
import com.codeshare.airline.identity.service.TenantService;
import com.codeshare.airline.identity.utils.mappers.AuthUserMapper;
import com.codeshare.airline.identity.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository UserRepository;
    private final UserMapper userMapper;
    private final AuthUserMapper authUserMapper;
    private final TenantRepository tenantRepository;
    private final TenantService tenantService;

    // -------------------------------------------------------------------------
    // CREATE NEW USER
    // -------------------------------------------------------------------------
    @Override
    public AuthUserDTO create(AuthUserDTO dto) {
        // 🔥 Fetch tenant entity
        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());

        // Check username uniqueness
        if (UserRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check email uniqueness
        if (UserRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User entity = userMapper.toEntity(dto);

        // If you use auditing, remove these two lines
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // Default security flags
        entity.setAccountNonLocked(true);
        entity.setEnabled(true);
        entity.setTenant(tenant);
        entity.setRecordStatus(RecordStatus.ACTIVE);

        User saved = UserRepository.save(entity);
        return userMapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // UPDATE USER
    // -------------------------------------------------------------------------
    @Override
    public AuthUserDTO update(UUID id, AuthUserDTO dto) {

        User entity = UserRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        // Email uniqueness check (if changed)
        if (dto.getEmail() != null && !dto.getEmail().equals(entity.getEmail())) {
            if (UserRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            entity.setEmail(dto.getEmail());
        }

        // ❗ Recommended: DO NOT allow username change — key login attribute
        if (dto.getUsername() != null && !dto.getUsername().equals(entity.getUsername())) {
            if (UserRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username already in use");
            }
            entity.setUsername(dto.getUsername());
        }

        // Update basic fields
        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
        entity.setEnabled(dto.isEnabled());
        if (!dto.isAccountNonLocked()) entity.setAccountNonLocked(dto.isAccountNonLocked());

        entity.setUpdatedAt(Instant.now());

        return userMapper.toDTO(UserRepository.save(entity));
    }


    // -------------------------------------------------------------------------
    // GET USER BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getById(UUID id) {

        User entity = UserRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        return userMapper.toDTO(entity);
    }


    // -------------------------------------------------------------------------
    // DELETE USER
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        User entity = UserRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        UserRepository.delete(entity);
    }


    // -------------------------------------------------------------------------
    // GET USER BY USERNAME
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getByUsername(String username) {

        User user = UserRepository.findByUsername(username)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + username));

        return userMapper.toDTO(user);
    }

    @Override
    public List<AuthUserDTO> getAllUsers() {
        List<User> userEntities = UserRepository.findAll();
        return userMapper.toDTOList(userEntities);
    }

    @Override
    public UserDetailsAdapter getAuthUserByUsername(String username) {
        User user = UserRepository.findByUsername(username)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + username));

        UserDetailsAdapter userDetailsAdapter = new UserDetailsAdapter(user.getId(), user.getUsername(),user.getEmail(), user.getPassword(), user.getTenant().getId(), user.getTenant().getTenantCode(), user.isEnabled(), user.isAccountNonLocked(), null);
        return userDetailsAdapter;
    }
}
