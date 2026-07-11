package com.codeshare.airline.identity.access.identity.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
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
        name = "groupss",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_group_code_tenant", columnNames = {"tenant_id", "code"})
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

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<GroupRole> groupRoles = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private Set<UserGroup> userGroups = new HashSet<>();
}
