package com.codeshare.airline.auth.entities.menu;

import com.codeshare.airline.common.utils.json.JsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Entity
@Table(name = "menus",
        indexes = {
                @Index(name = "idx_menus_tenant", columnList = "tenant_id"),
                @Index(name = "idx_menus_parent", columnList = "parent_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String code;


    @Column(length = 500)
    private String url;

    @Column(length = 200)
    private String icon;

    @Column(nullable = false)
    private String title;

    // JSON fields
    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> iconComponent;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> badge;

    // Hierarchical menu structure
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parentMenu;

    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> children = new ArrayList<>();

    // FIXED → store only tenantId
    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    private UUID tenantId;

    // FIXED → store organizationId as UUID only (optional)
    @Column(name = "organization_id", columnDefinition = "uuid")
    private UUID organizationId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuRole> menuRoles = new HashSet<>();
}

