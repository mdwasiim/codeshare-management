package com.codeshare.airline.auth.repository;


import com.codeshare.airline.auth.entities.authToken.PasswordResetToken;
import com.codeshare.airline.common.jpa.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
}

