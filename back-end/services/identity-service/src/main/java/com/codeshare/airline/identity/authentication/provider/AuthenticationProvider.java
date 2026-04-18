package com.codeshare.airline.identity.authentication.provider;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.authentication.api.request.LoginRequest;
import com.codeshare.airline.identity.authentication.service.core.AuthenticationResult;

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



