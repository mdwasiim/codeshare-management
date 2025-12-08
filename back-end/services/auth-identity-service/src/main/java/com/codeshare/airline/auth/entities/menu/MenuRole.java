package com.codeshare.airline.auth.entities.menu;

import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "menu_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_role",
                        columnNames = {"tenant_id", "menu_id", "role_id"}
                )
        },
        indexes = {
                @Index(name = "idx_menu_role_tenant", columnList = "tenant_id"),
                @Index(name = "idx_menu_role_menu", columnList = "menu_id"),
                @Index(name = "idx_menu_role_role", columnList = "role_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MenuRole extends AbstractEntity {

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

