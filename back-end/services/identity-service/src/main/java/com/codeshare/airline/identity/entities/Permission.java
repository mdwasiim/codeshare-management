package com.codeshare.airline.identity.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_domain_action_tenant",
                        columnNames = {"tenant_id", "domain", "action"}
                )
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
    private String name; // or displayName

    @EqualsAndHashCode.Include
    @Column(name = "code", nullable = false, length = 150)
    private String code;   // USER:CREATE

    @Column(name = "domain", nullable = false, length = 100)
    private String domain; // USER

    @Column(name = "action", nullable = false, length = 100)
    private String action; // CREATE

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<RolePermission> rolePermissions = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (domain != null) domain = domain.toUpperCase();
        if (action != null) action = action.toUpperCase();
        this.code = domain + ":" + action;
    }
}