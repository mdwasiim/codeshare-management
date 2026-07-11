package com.codeshare.airline.identity.access.assignments.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.identity.entities.Group;
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

import java.util.UUID;

@Entity
@Table(
        name = "group_menus",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_group_menu", columnNames = {"tenant_id", "group_id", "menu_id"})
        },
        indexes = {
                @Index(name = "idx_group_menu_tenant", columnList = "tenant_id"),
                @Index(name = "idx_group_menu_group", columnList = "group_id"),
                @Index(name = "idx_group_menu_menu", columnList = "menu_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
public class GroupMenu extends CSMDataAbstractEntity {

    @EqualsAndHashCode.Include
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private Group group;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    @ToString.Exclude
    private Menu menu;
}
