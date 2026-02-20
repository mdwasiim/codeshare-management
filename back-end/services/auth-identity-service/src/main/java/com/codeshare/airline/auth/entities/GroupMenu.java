package com.codeshare.airline.auth.entities;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(
        name = "group_menus",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_group_menu",
                        columnNames = {"tenant_id", "group_id", "menu_id"}
                )
        },
        indexes = {
                @Index(name = "idx_group_menu_tenant", columnList = "tenant_id"),
                @Index(name = "idx_group_menu_group", columnList = "group_id"),
                @Index(name = "idx_group_menu_menu", columnList = "menu_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class GroupMenu extends CSMDataAbstractEntity {

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
    @JoinColumn(name = "menu_id", nullable = false)
    @ToString.Exclude
    private Menu menu;
}

