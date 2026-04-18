package com.codeshare.airline.data.entity;

import com.codeshare.airline.core.dto.audit.context.CSMAuditContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

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
        Instant now = now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        this.updatedAt = now;

        CSMAuditContext ctx = CSMAuditContext.get();

        String user = resolveUser(ctx);
        String txn  = resolveTxn(ctx);

        if (this.createdBy == null) {
            this.createdBy = user;
        }

        this.updatedBy = user;

        this.transactionId = (txn != null)
                ? txn
                : UUID.randomUUID().toString();
    }

    @PreUpdate
    protected void onUpdate() {
        Instant now = now();
        this.updatedAt = now;

        CSMAuditContext ctx = CSMAuditContext.get();

        String user = resolveUser(ctx);
        String txn  = resolveTxn(ctx);

        this.updatedBy = user;

        if (txn != null) {
            this.transactionId = txn;
        }
    }

    /* ---------- helpers ---------- */

    private Instant now() {
        return Instant.now();
    }

    private String resolveUser(CSMAuditContext ctx) {
        return (ctx != null && ctx.getUserId() != null)
                ? ctx.getUserId()
                : "SYSTEM";
    }

    private String resolveTxn(CSMAuditContext ctx) {
        return (ctx != null) ? ctx.getTransactionId() : null;
    }
}
