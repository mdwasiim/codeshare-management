package com.codeshare.airline.common.audit;

import com.codeshare.airline.common.httpTransaction.AuditUserProvider;
import com.codeshare.airline.common.httpTransaction.TransactionIdProvider;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditEntityListener {

    private String getCurrentUser() {
        // Priority: explicit provider (settable by app), then Spring Security, then fallback "system"
        try {
            String user = AuditUserProvider.get();
            if (user != null && !user.isBlank()) return user;
        } catch (Exception ignored) {}

        try {
            if (SecurityContextHolder.getContext() != null &&
                    SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().getName() != null) {
                return SecurityContextHolder.getContext().getAuthentication().getName();
            }
        } catch (Exception ignored) {}

        return "system";
    }

    @PrePersist
    public void prePersist(Object target) {
        if (target instanceof Auditable entity) {
            LocalDateTime now = LocalDateTime.now();
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            String user = getCurrentUser();
            entity.setCreatedBy(user);
            entity.setUpdatedBy(user);

            entity.setTransactionId(TransactionIdProvider.get());
        }
    }

    @PreUpdate
    public void preUpdate(Object target) {
        if (target instanceof Auditable entity) {
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(getCurrentUser());

            // transaction id remains the one for the request - set again to be safe
            entity.setTransactionId(TransactionIdProvider.get());
        }
    }
}
