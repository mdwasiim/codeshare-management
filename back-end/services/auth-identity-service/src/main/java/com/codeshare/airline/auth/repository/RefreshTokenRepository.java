package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.model.entities.RefreshToken;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    List<RefreshToken> findByTenantIdAndUsernameAndActiveTrue(UUID tenantId, String username);

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
