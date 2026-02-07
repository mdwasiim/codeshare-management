package com.codeshare.airline.auth.model.entities;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "groupss",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_code_tenant",
                        columnNames = {"tenant_id", "code"}
                )
        },
        indexes = {
                @Index(name = "idx_group_tenant", columnList = "tenant_id"),
                @Index(name = "idx_group_tenant_code", columnList = "tenant_id, code")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Group extends CSMDataAbstractEntity {

    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    /* Tenant boundary (CONSISTENT) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @ToString.Exclude
    private Tenant tenant;

    /* relationships */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<UserGroup> UserGroups = new HashSet<>();
}

