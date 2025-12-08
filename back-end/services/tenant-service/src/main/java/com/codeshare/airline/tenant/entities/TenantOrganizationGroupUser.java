package com.codeshare.airline.tenant.entities;

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
        name = "tenant_group_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_user",
                        columnNames = {"group_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantOrganizationGroupUser extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private TenantOrganizationGroup group;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId; // user belongs to auth-identity-service
}
