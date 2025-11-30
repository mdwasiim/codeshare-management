package com.codeshare.airline.auth.entities.menu;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.common.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "menu_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "role_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRole extends AbstractEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
