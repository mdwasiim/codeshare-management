package com.codeshare.airline.identity.access.authentication.core.provider;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationResult;

public interface AuthenticationProvider {

    /**
     * Get supported AuthSource
     */
    AuthSource getAuthSource();

    /**
     * Perform authentication
     */
    AuthenticationResult authenticate(LoginRequest request);

}



