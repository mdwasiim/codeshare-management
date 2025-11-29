package com.codeshare.airline.common.audit;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuditEntityListener {

    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "system";
        }
    }

    @PrePersist
    public void prePersist(Object obj) {
        if (obj instanceof Auditable entity) {
            LocalDateTime now = LocalDateTime.now();
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            String user = getCurrentUser();
            entity.setCreatedBy(user);
            entity.setUpdatedBy(user);
        }
    }

    @PreUpdate
    public void preUpdate(Object obj) {
        if (obj instanceof Auditable entity) {
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(getCurrentUser());
        }
    }
}
