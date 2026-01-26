package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.auth.entities.User;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.AuthUserService;
import com.codeshare.airline.auth.utils.mappers.AuthUserMapper;
import com.codeshare.airline.auth.utils.mappers.UserMapper;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository UserRepository;
    private final UserMapper userMapper;
    private final AuthUserMapper authUserMapper;

    // -------------------------------------------------------------------------
    // CREATE NEW USER
    // -------------------------------------------------------------------------
    @Override
    public AuthUserDTO create(AuthUserDTO dto) {

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
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // Default security flags
        entity.setAccountNonLocked(true);
        entity.setEnabled(true);

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

        entity.setUpdatedAt(LocalDateTime.now());

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

        UserDetailsAdapter userDetailsAdapter = new UserDetailsAdapter(user.getId(), user.getUsername(), user.getTenant().getId(), user.getTenant().getTenantCode(), user.isEnabled(), user.isAccountNonLocked(), null);
        return userDetailsAdapter;
    }
}
