package com.codeshare.airline.auth.entities.identity;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "user_organization",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_tenant_org",
                columnNames = {"user_id", "tenant_id", "organization_id"}
        ),
        indexes = {
                @Index(name = "idx_uo_user", columnList = "user_id"),
                @Index(name = "idx_uo_tenant", columnList = "tenant_id"),
                @Index(name = "idx_uo_org", columnList = "organization_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserOrganization extends AbstractEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "organization_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID organizationId;

    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;
}
