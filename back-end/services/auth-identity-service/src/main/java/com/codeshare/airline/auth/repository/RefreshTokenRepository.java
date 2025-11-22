package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.auth.RefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
