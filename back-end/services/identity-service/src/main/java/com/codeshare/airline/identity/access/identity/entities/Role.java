package com.codeshare.airline.identity.access.identity.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_code_tenant", columnNames = {"tenant_id", "code"})
        },
        indexes = {
                @Index(name = "idx_role_tenant", columnList = "tenant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class Role extends CSMDataAbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<RolePermission> rolePermissions = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (code != null) code = code.toUpperCase();
    }
}
