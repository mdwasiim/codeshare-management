package com.codeshare.airline.auth.entities.menu;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "menu_role",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_menu_role", columnNames = {"menu_id", "role_id"})
        },
        indexes = {
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
