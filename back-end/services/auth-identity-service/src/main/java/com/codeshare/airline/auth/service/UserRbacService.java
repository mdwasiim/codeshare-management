package com.codeshare.airline.auth.service;

import com.codeshare.airline.auth.model.UserRbacResponse;

import java.util.UUID;

public interface UserRbacService {

    UserRbacResponse resolveUserRbac(UUID userId);

    void invalidateUserRbacCache(UUID userId);
}
