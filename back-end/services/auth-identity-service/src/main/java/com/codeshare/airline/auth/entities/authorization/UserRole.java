package com.codeshare.airline.auth.entities.authorization;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "user_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole extends AbstractEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
