package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.RefreshToken;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
