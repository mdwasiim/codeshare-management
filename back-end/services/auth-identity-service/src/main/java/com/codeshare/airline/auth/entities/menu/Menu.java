package com.codeshare.airline.auth.entities.menu;

import com.codeshare.airline.common.services.jpa.AbstractEntity;
import com.codeshare.airline.common.services.utils.json.JsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(
        name = "menus",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_menu_code_tenant_org", columnNames = {"tenant_id", "organization_id", "code"})
        },
        indexes = {
                @Index(name = "idx_menus_tenant", columnList = "tenant_id"),
                @Index(name = "idx_menus_parent", columnList = "parent_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends AbstractEntity {

    // -------------------------------
    // Basic Menu Fields
    // -------------------------------
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", length = 200)
    private String code;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "icon", length = 200)
    private String icon;

    @Column(name = "title", length = 200)
    private String title;

    // -------------------------------
    // JSON Config Fields
    // -------------------------------
    @Convert(converter = JsonConverter.class)
    @Column(name = "icon_component", columnDefinition = "json")
    private Map<String, String> iconComponent;

    @Convert(converter = JsonConverter.class)
    @Column(name = "badge", columnDefinition = "json")
    private Map<String, String> badge;

    @Convert(converter = JsonConverter.class)
    @Column(name = "attributes", columnDefinition = "json")
    private Map<String, String> attributes;

    // -------------------------------
    // Menu Hierarchy (Tree Structure)
    // -------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parentMenu;

    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> children = new ArrayList<>();

    // -------------------------------
    // Multi-Tenant & Multi-Org Support
    // -------------------------------
    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "organization_id", columnDefinition = "BINARY(16)")
    private UUID organizationId;

    // -------------------------------
    // RBAC Relation
    // -------------------------------
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuRole> menuRoles = new HashSet<>();
}
