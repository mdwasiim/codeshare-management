package com.codeshare.airline.common.jpa.audit;

import com.codeshare.airline.common.utils.mapper.audit.Auditable;
import com.codeshare.airline.common.httpTransaction.AuditUserProvider;
import com.codeshare.airline.common.httpTransaction.TransactionIdProvider;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuditEntityListener {

    private String getCurrentUser() {
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
            String user = getCurrentUser();

            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entity.setCreatedBy(user);
            entity.setUpdatedBy(user);

            entity.setIsDeleted(false);
            entity.setActive(true);

            entity.setTransactionId(TransactionIdProvider.get());
        }
    }

    @PreUpdate
    public void preUpdate(Object target) {
        if (target instanceof Auditable entity) {
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(getCurrentUser());
            entity.setTransactionId(TransactionIdProvider.get());
        }
    }

    @PreRemove
    public void preRemove(Object target) {
        if (target instanceof Auditable entity) {
            LocalDateTime now = LocalDateTime.now();
            String user = getCurrentUser();

            entity.setIsDeleted(true);
            entity.setDeletedAt(now);
            entity.setDeletedBy(user);

            // active automatically becomes false on delete
            entity.setActive(false);

            entity.setUpdatedAt(now);
            entity.setUpdatedBy(user);

            entity.setTransactionId(TransactionIdProvider.get());
        }
    }
}
