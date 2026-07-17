package com.codeshare.airline.identity.access.assignments.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@Table(
        name = "group_roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_group_role", columnNames = {"tenant_id", "group_id", "role_id"})
        },
        indexes = {
                @Index(name = "idx_group_role_tenant", columnList = "tenant_id"),
                @Index(name = "idx_group_role_group", columnList = "group_id"),
                @Index(name = "idx_group_role_role", columnList = "role_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class GroupRole extends CSMDataAbstractEntity {

    @EqualsAndHashCode.Include
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private Group group;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private Role role;
}
