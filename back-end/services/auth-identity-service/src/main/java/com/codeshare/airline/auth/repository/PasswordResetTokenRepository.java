package com.codeshare.airline.auth.repository;


import com.codeshare.airline.auth.entities.authentication.PasswordResetToken;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
}

