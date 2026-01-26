package com.codeshare.airline.auth.entities;


import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_user_group",
                        columnNames = {"tenant_id", "user_id", "group_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_group_tenant", columnList = "tenant_id"),
                @Index(name = "idx_user_group_user", columnList = "user_id"),
                @Index(name = "idx_user_group_id", columnList = "group_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class UserGroup extends CSMDataAbstractEntity {

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
