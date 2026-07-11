package com.codeshare.airline.identity.access.authorization.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "menus",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_menu_code_tenant_org", columnNames = {"tenant_id", "code"})
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
@ToString(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends CSMDataAbstractEntity {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label", nullable = false, length = 200)
    private String label;

    @Column(name = "topbar_label", length = 200)
    private String topbarLabel;

    @Column(name = "sidebar_label", length = 200)
    private String sidebarLabel;

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

    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> childMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMenu> groupMenus = new ArrayList<>();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
