package com.codeshare.airline.auth.entities.rbac;

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
        name = "group_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_role",
                        columnNames = {"tenant_id", "group_id", "role_id"}
                )
        },
        indexes = {
                @Index(name = "idx_group_role_tenant", columnList = "tenant_id"),
                @Index(name = "idx_group_role_group", columnList = "group_id"),
                @Index(name = "idx_group_role_role", columnList = "role_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GroupRole extends AbstractEntity {

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
