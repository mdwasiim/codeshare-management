package com.codeshare.airline.identity.entities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "menus",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_code_tenant_org",
                        columnNames = {"tenant_id", "code"}
                )
        },
        indexes = {
                @Index(name = "idx_menus_tenant", columnList = "tenant_id"),
                @Index(name = "idx_menus_parent", columnList = "parent_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends CSMDataAbstractEntity {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label", nullable = false, length = 200)
    private String label;

    @Column(name = "icon", length = 200)
    private String icon;

    @Column(name = "route", length = 1000)
    private String route;

    @Column(name = "permission", length = 150)
    private String permission;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "visible")
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    @ToString.Exclude
    private Menu parentMenu;

    // parent-child menus
    @OneToMany(
            mappedBy = "parentMenu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Menu> childMenus = new ArrayList<>();

    // group-menu mappings
    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GroupMenu> groupMenus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Tenant tenant;
}

