package com.codeshare.airline.auth.entities;

import com.codeshare.airline.persistence.converters.CSMListDataToJsonConverter;
import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
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

    @Column(name = "label", nullable = false, length = 200)
    private String label;

    @Column(name = "icon", length = 200)
    private String icon;

    @Convert(converter = CSMListDataToJsonConverter.class)
    @Column(name = "router_link", nullable = false, length = 1000)
    @Builder.Default
    private List<String> routerLink = new ArrayList<>();

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    @ToString.Exclude
    private Menu parentMenu;

    @OneToMany(
            mappedBy = "parentMenu",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    @ToString.Exclude
    private final List<Menu> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Tenant tenant;
}

