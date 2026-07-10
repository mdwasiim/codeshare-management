package com.codeshare.airline.identity.access.identity.service.serviceImpl;


import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.access.identity.repository.UserRepository;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.identity.access.identity.service.TenantService;
import com.codeshare.airline.identity.access.identity.mappers.AuthUserMapper;
import com.codeshare.airline.identity.access.identity.mappers.UserMapper;
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
    private final TenantRepository tenantRepository;
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;

    // -------------------------------------------------------------------------
    // CREATE NEW USER
    // -------------------------------------------------------------------------
    @Override
    public AuthUserDTO create(AuthUserDTO dto) {

        Tenant tenant = tenantService.getTenantByTenantCode(
                TenantContextHolder.getTenant().getTenantCode()
        );

        // Username uniqueness
        if (userRepository.existsByUsernameAndTenant_Id(dto.getUsername(), tenant.getId())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Email uniqueness
        if (userRepository.existsByEmailAndTenant_Id(dto.getEmail(), tenant.getId())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User entity = userMapper.toEntity(dto);

        // If auditing exists, remove these
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // Security defaults
        entity.setEnabled(true);
        entity.setActive(true);

        entity.setAccountNonLocked(true);
        entity.setAccountNonExpired(true);
        entity.setCredentialsNonExpired(true);

        entity.setAuthSource(AuthSource.INTERNAL);
        entity.setExternalId("internal:");

        entity.setTenant(tenant);
        entity.setRecordStatus(RecordStatus.ACTIVE);

        // Encode password
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User saved = userRepository.save(entity);

        return userMapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // UPDATE USER
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
// UPDATE USER
// -------------------------------------------------------------------------
    @Override
    public AuthUserDTO update(UUID id, AuthUserDTO dto) {

        User entity = userRepository.findById(id)
                .orElseThrow(() ->
                        new CSMResourceNotFoundException("User not found: " + id)
                );

        // Email uniqueness check
        if (dto.getEmail() != null &&
                !dto.getEmail().equals(entity.getEmail())) {

            if (userRepository.existsByEmailAndTenant_Id(dto.getEmail(), entity.getTenant().getId())) {
                throw new IllegalArgumentException("Email already in use");
            }

            entity.setEmail(dto.getEmail());
        }

        // Username uniqueness check
        if (dto.getUsername() != null &&
                !dto.getUsername().equals(entity.getUsername())) {

            if (userRepository.existsByUsernameAndTenant_Id(dto.getUsername(), entity.getTenant().getId())) {
                throw new IllegalArgumentException("Username already in use");
            }

            entity.setUsername(dto.getUsername());
        }

        // Basic fields
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }

        // Password update
        if (dto.getPassword() != null &&
                !dto.getPassword().isBlank()) {

            entity.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        // Boolean updates
        entity.setEnabled(dto.isEnabled());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setAccountNonExpired(dto.isAccountNonExpired());
        entity.setCredentialsNonExpired(dto.isCredentialsNonExpired());

        entity.setUpdatedAt(Instant.now());

        User updated = userRepository.save(entity);

        return userMapper.toDTO(updated);
    }

    // -------------------------------------------------------------------------
    // GET USER BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getById(UUID id) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        return userMapper.toDTO(entity);
    }


    // -------------------------------------------------------------------------
    // DELETE USER
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        User entity = userRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + id));

        userRepository.delete(entity);
    }


    // -------------------------------------------------------------------------
    // GET USER BY USERNAME
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getByUsername(String username) {
        User user = resolveUser(username)
                .orElseThrow(() -> new CSMResourceNotFoundException("User not found: " + username));

        return userMapper.toDTO(user);
    }

    @Override
    public List<AuthUserDTO> getAllUsers() {
        List<User> userEntities = userRepository.findAll();
        return userMapper.toDTOList(userEntities);
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
        User user = userRepository.findByExternalIdAndTenant_TenantCodeAndAuthSource(externalId, tenantCode, authSource)
                .or(() -> userRepository.findByUsernameAndTenant_TenantCode(username, tenantCode))
                .or(() -> userRepository.findByEmailAndTenant_TenantCode(email, tenantCode))
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
        if (tenantContext != null && tenantContext.getTenantCode() != null) {
            return userRepository.findByUsernameAndTenant_TenantCode(username, tenantContext.getTenantCode());
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
        return new UserDetailsAdapter(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getTenant().getId(),
                user.getTenant().getTenantCode(),
                user.isEnabled(),
                user.isAccountNonLocked(),
                null
        );
    }
}
