package com.codeshare.airline.tenant.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tenant_data_source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDataSource  {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String dbVendor;      // POSTGRES, MYSQL, ORACLE, SQLSERVER

    @Column(nullable = false, length = 50)
    private String dbType;        // PRIMARY, REPLICA, ANALYTICS, ARCHIVE

    @Column(nullable = false, length = 200)
    private String driverClass;   // org.postgresql.Driver etc.

    @Column(nullable = false, length = 200)
    private String dialect;       // org.hibernate.dialect.PostgreSQLDialect etc.

    @Column(nullable = false, length = 500)
    private String dbUrl;         // ✔ REQUIRED for actual DB connection

    @Column(nullable = false, length = 200)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(length = 100)
    private String defaultPort;

    @Column(length = 300)
    private String description;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
}


