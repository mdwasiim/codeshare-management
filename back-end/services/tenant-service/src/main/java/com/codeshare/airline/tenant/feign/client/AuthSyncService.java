package com.codeshare.airline.tenant.feign.client;

import com.codeshare.airline.common.auth.identity.model.TenantGroupSyncDTO;
import com.codeshare.airline.common.services.response.ServiceError;
import com.codeshare.airline.common.services.response.ServiceResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSyncService {

    private final AuthGroupSyncClient client;

    /**
     * Sync group with Auth-Identity Service (resilient)
     */
    @CircuitBreaker(name = "authService", fallbackMethod = "fallbackSyncGroup")
    @Retry(name = "authService")
    public ServiceResponse syncGroup(TenantGroupSyncDTO dto) {
        return client.syncGroup(dto);
    }

    /**
     * Fallback executed when Feign client fails due to:
     * - Timeout
     * - CircuitBreaker open
     * - Connection refused
     * - Service unavailable
     */
    public ServiceResponse fallbackSyncGroup(TenantGroupSyncDTO dto, Throwable ex) {
        return ServiceResponse.error(
                ServiceError.builder()
                        .message("Auth service unavailable: " + ex.getMessage())
                        .build()
        );
    }
}
