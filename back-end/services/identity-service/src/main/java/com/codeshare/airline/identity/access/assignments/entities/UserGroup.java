package com.codeshare.airline.identity.access.assignments.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_user_group", columnNames = {"tenant_id", "user_id", "group_id"})
        },
        indexes = {
                @Index(name = "idx_user_group_tenant", columnList = "tenant_id"),
                @Index(name = "idx_user_group_user", columnList = "user_id"),
                @Index(name = "idx_user_group_id", columnList = "group_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class UserGroup extends CSMDataAbstractEntity {

    @EqualsAndHashCode.Include
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private Group group;
}
