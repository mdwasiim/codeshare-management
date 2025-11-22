package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.utils.mappers.UserMapper;
import com.codeshare.airline.auth.repository.UserRepository;
import com.codeshare.airline.auth.service.UserService;
import com.codeshare.airline.common.auth.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDTO create(UserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User entity = mapper.toEntity(dto);

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setAccountNonLocked(true);
        entity.setEnabled(true);

        User saved = userRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public UserDTO update(UUID id, UserDTO dto) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        entity.setEnabled(dto.isEnabled());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public UserDTO getById(UUID id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDTO(entity);
    }

    @Override
    public List<UserDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(userRepository.findByTenantId(tenantId));
    }

    @Override
    public List<UserDTO> getByOrganization(UUID orgId) {
        return mapper.toDTOList(userRepository.findByOrganizationId(orgId));
    }

    @Override
    public void delete(UUID id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(entity);
    }


}
