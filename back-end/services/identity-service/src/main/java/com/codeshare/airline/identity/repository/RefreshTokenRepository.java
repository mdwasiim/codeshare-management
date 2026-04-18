package com.codeshare.airline.identity.repository;

import com.codeshare.airline.identity.entities.RefreshToken;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
