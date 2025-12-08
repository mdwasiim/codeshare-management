package com.codeshare.airline.auth.entities.audit;


import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.services.jpa.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "login_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginAudit extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // nullable for failed logins

    @Column(name = "username", length = 150)
    private String username;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "success")
    private boolean success;

    @Column(name = "message", length = 500)
    private String message;
}
