package com.codeshare.airline.identity.access.authorization.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
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

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_permission_domain_action_tenant", columnNames = {"tenant_id", "domain", "action"})
        },
        indexes = {
                @Index(name = "idx_permission_tenant", columnList = "tenant_id"),
                @Index(name = "idx_permission_domain_action", columnList = "domain, action")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class Permission extends CSMDataAbstractEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "code", nullable = false, length = 150)
    private String code;

    @Column(name = "domain", nullable = false, length = 100)
    private String domain;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<RolePermission> rolePermissions = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (domain != null) domain = domain.toUpperCase();
        if (action != null) action = action.toUpperCase();
        this.code = domain + ":" + action;
    }
}
