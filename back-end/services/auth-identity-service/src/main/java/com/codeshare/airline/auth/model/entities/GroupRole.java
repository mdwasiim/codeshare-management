package com.codeshare.airline.auth.model.entities;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "group_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_group_role",
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class GroupRole extends CSMDataAbstractEntity {

    /* Tenant boundary — REQUIRED */
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @ToString.Exclude
    private Tenant tenant;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private Group group;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private Role role;
}

