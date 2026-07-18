package com.codeshare.airline.identity.access.authorization.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.platform.core.enums.tenant.MenuNavigationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "navigation_type", nullable = false, length = 30)
    private MenuNavigationType navigationType = MenuNavigationType.SECTION;

    /**
     * Angular route path, for example "/groups/operations".
     * This is not a backend API path. Only INTERNAL_LINK rows should use it.
     */
    @Column(name = "frontend_path", length = 1000)
    private String frontendPath;

    /**
     * Absolute URL for links outside the Angular router.
     * Only EXTERNAL_LINK rows should use it.
     */
    @Column(name = "external_url", length = 1000)
    private String externalUrl;

    /**
     * Permission code required to display this menu item, for example "group:assign".
     * Null means visibility is controlled only by group-menu assignment.
     */
    @Column(name = "permission_code", length = 150)
    private String permissionCode;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Builder.Default
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    @ToString.Exclude
    private Menu parentMenu;

    @Builder.Default
    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> childMenus = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMenu> groupMenus = new ArrayList<>();

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @PrePersist
    @PreUpdate
    void normalizeNavigationType() {
        if (navigationType == null) {
            navigationType = hasText(frontendPath) ? MenuNavigationType.INTERNAL_LINK : MenuNavigationType.SECTION;
        }

        if (navigationType == MenuNavigationType.SECTION) {
            frontendPath = null;
            externalUrl = null;
        } else if (navigationType == MenuNavigationType.INTERNAL_LINK) {
            externalUrl = null;
        } else if (navigationType == MenuNavigationType.EXTERNAL_LINK) {
            frontendPath = null;
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
