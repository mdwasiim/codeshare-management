package com.codeshare.airline.persistence.persistence.entity;

import com.codeshare.airline.core.dto.audit.context.CSMAuditContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CSMDataAuditableEntity {

    /* ---------- Creation ---------- */

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    /* ---------- Update ---------- */

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /* ---------- State ---------- */

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    /* ---------- Trace ---------- */

    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    /* ---------- Lifecycle hooks ---------- */

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        this.updatedAt = now;

        var ctx = CSMAuditContext.get();
        if (ctx != null) {
            String user = ctx.getUserId() != null ? ctx.getUserId() : "SYSTEM";
            String txn  = ctx.getTransactionId();

            if (this.createdBy == null) {
                this.createdBy = user;
            }

            this.updatedBy = user;

            if (txn != null) {
                this.transactionId = txn;
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        Instant now = Instant.now();
        this.updatedAt = now;

        var ctx = CSMAuditContext.get();
        if (ctx != null) {
            String user = ctx.getUserId() != null ? ctx.getUserId() : "SYSTEM";
            String txn  = ctx.getTransactionId();

            this.updatedBy = user;

            if (txn != null) {
                this.transactionId = txn;
            }
        }
    }
}
