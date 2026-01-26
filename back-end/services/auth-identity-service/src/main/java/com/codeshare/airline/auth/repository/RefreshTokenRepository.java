package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.RefreshToken;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
