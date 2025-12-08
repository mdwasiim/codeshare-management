package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserOrganization;
import com.codeshare.airline.auth.repository.UserOrganizationRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.UserService;
import com.codeshare.airline.auth.utils.mappers.UserMapper;
import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserOrganizationRepository userOrganizationRepository;
    private final UserMapper mapper;


    // -------------------------------------------------------------------------
    // CREATE NEW USER
    // -------------------------------------------------------------------------
    @Override
    public UserDTO create(UserDTO dto) {

        // Check username uniqueness
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check email uniqueness
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User entity = mapper.toEntity(dto);

        // If you use auditing, remove these two lines
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // Default security flags
        entity.setAccountNonLocked(true);
        entity.setEnabled(true);

        User saved = userRepository.save(entity);
        return mapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // UPDATE USER
    // -------------------------------------------------------------------------
    @Override
    public UserDTO update(UUID id, UserDTO dto) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        // Email uniqueness check (if changed)
        if (dto.getEmail() != null && !dto.getEmail().equals(entity.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            entity.setEmail(dto.getEmail());
        }

        // ❗ Recommended: DO NOT allow username change — key login attribute
        if (dto.getUsername() != null && !dto.getUsername().equals(entity.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
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

        return mapper.toDTO(userRepository.save(entity));
    }


    // -------------------------------------------------------------------------
    // GET USER BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(UUID id) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        return mapper.toDTO(entity);
    }


    // -------------------------------------------------------------------------
    // GET ALL USERS FOR A TENANT
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(userRepository.findByTenantId(tenantId));
    }


    // -------------------------------------------------------------------------
    // GET USERS BY ORGANIZATION
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getByOrganization(UUID orgId) {

        List<UserOrganization> mappings = userOrganizationRepository.findByOrganizationId(orgId);

        List<User> users = mappings.stream()
                .map(UserOrganization::getUser)
                .distinct() // ensures no duplicates
                .collect(Collectors.toList());

        return mapper.toDTOList(users);
    }


    // -------------------------------------------------------------------------
    // DELETE USER
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        userRepository.delete(entity);
    }


    // -------------------------------------------------------------------------
    // GET USER BY USERNAME
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public UserDTO getByUsername(String username) {

        User entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        return mapper.toDTO(entity);
    }
}
