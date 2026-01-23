package com.codeshare.airline.auth.model.entities;

import com.codeshare.airline.persistence.converters.CSMDataJsonConverter;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu extends CSMDataAbstractEntity {

    /* Basic fields */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "icon", length = 200)
    private String icon;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    /* JSON config */
    @Builder.Default
    @Convert(converter = CSMDataJsonConverter.class)
    @Column(name = "icon_component", columnDefinition = "json")
    private Map<String, String> iconComponent = new HashMap<>();

    @Builder.Default
    @Convert(converter = CSMDataJsonConverter.class)
    @Column(name = "badge", columnDefinition = "json")
    private Map<String, String> badge = new HashMap<>();

    @Builder.Default
    @Convert(converter = CSMDataJsonConverter.class)
    @Column(name = "attributes", columnDefinition = "json")
    private Map<String, String> attributes = new HashMap<>();

    /* Hierarchy */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Menu parentMenu;

    @OneToMany(
            mappedBy = "parentMenu",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @ToString.Exclude
    private List<Menu> children = new ArrayList<>();

    /* Tenant & org boundary — CONSISTENT */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @ToString.Exclude
    private Tenant tenant;
}

