package com.codeshare.airline.identity.access.identity.service.serviceImpl;

import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.mappers.AuthUserMapper;
import com.codeshare.airline.identity.access.identity.mappers.UserMapper;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthUserMapper authUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDTO create(AuthUserDTO dto) {
        UUID tenantId = TenantContextHolder.getTenant().getId();

        if (userRepository.existsByUsernameAndTenantId(dto.getUsername(), tenantId)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmailAndTenantId(dto.getEmail(), tenantId)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User entity = userMapper.toEntity(dto);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setEnabled(true);
        entity.setActive(true);
        entity.setAccountNonLocked(true);
        entity.setAccountNonExpired(true);
        entity.setCredentialsNonExpired(true);
        entity.setAuthSource(AuthSource.INTERNAL);
        entity.setExternalId("internal:");
        entity.setTenantId(tenantId);
        entity.setRecordStatus(RecordStatus.ACTIVE);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public AuthUserDTO update(UUID id, AuthUserDTO dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(entity.getEmail())) {
            if (userRepository.existsByEmailAndTenantId(dto.getEmail(), entity.getTenantId())) {
                throw new IllegalArgumentException("Email already in use");
            }
            entity.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null && !dto.getUsername().equals(entity.getUsername())) {
            if (userRepository.existsByUsernameAndTenantId(dto.getUsername(), entity.getTenantId())) {
                throw new IllegalArgumentException("Username already in use");
            }
            entity.setUsername(dto.getUsername());
        }

        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        entity.setEnabled(dto.isEnabled());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setAccountNonExpired(dto.isAccountNonExpired());
        entity.setCredentialsNonExpired(dto.isCredentialsNonExpired());
        entity.setUpdatedAt(Instant.now());

        return userMapper.toDTO(userRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getById(UUID id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));
        return userMapper.toDTO(entity);
    }

    @Override
    public void delete(UUID id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));
        userRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getByUsername(String username) {
        User user = resolveUser(username)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + username));
        return userMapper.toDTO(user);
    }

    @Override
    public List<AuthUserDTO> getAllUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    @Override
    public UserDetailsAdapter getAuthUserByUsername(String username) {
        User user = resolveUser(username)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + username));
        return toUserDetails(user);
    }

    @Override
    public UserDetailsAdapter getAuthUserForFederatedLogin(
            String tenantCode,
            AuthSource authSource,
            String externalId,
            String username,
            String email
    ) {
        UUID tenantId = TenantContextHolder.getTenant().getId();
        User user = userRepository.findByExternalIdAndTenantIdAndAuthSource(externalId, tenantId, authSource)
                .or(() -> userRepository.findByUsernameAndTenantId(username, tenantId))
                .or(() -> userRepository.findByEmailAndTenantId(email, tenantId))
                .orElseThrow(() -> new CSMResourceNotFoundException("Federated user not found for tenant: " + tenantCode));

        if ((user.getExternalId() == null || user.getExternalId().isBlank()) && externalId != null && !externalId.isBlank()) {
            user.setExternalId(externalId);
            user.setAuthSource(authSource);
            userRepository.save(user);
        }

        return toUserDetails(user);
    }

    private java.util.Optional<User> resolveUser(String username) {
        TenantContext tenantContext = getCurrentTenantContext();
        if (tenantContext != null && tenantContext.getId() != null) {
            return userRepository.findByUsernameAndTenantId(username, tenantContext.getId());
        }
        return userRepository.findByUsername(username);
    }

    private TenantContext getCurrentTenantContext() {
        try {
            return TenantContextHolder.getTenant();
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    private UserDetailsAdapter toUserDetails(User user) {
        String tenantCode = getCurrentTenantContext() != null ? getCurrentTenantContext().getTenantCode() : null;
        return new UserDetailsAdapter(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getTenantId(),
                tenantCode,
                user.isEnabled(),
                user.isAccountNonLocked(),
                null
        );
    }
}
