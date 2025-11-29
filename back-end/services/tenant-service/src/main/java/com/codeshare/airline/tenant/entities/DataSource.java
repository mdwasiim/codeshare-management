package com.codeshare.airline.tenant.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "mst_data_source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSource {

    @Id
    // @GeneratedValue
    //@UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    /* -------------------------------
       Core DB Definition
    ------------------------------- */
    @Column(nullable = false)
    private String dbVendor;          // MYSQL, POSTGRES, ORACLE, MARIADB, MSSQL, H2, DB2, SQLITE

    @Column(nullable = false)
    private String dbType;            // PRIMARY, SECONDARY, ANALYTICS

    private String driverClass;
    private String dialect;

    /* -------------------------------
       Connection Details
    ------------------------------- */
    @Column(nullable = false, length = 500)
    private String dbUrl;

    private String username;
    private String password;

    /* Extracted / useful connection metadata */
    private String host;              // e.g. localhost
    private Integer port;             // e.g. 5432
    private String databaseName;      // e.g. qr_db
    private String connectionParams;


    /* -------------------------------
       Pooling Settings (Optional)
    ------------------------------- */
    private String poolName;
    private Integer maxPoolSize;
    private Integer minIdle;
    private Integer connectionTimeout;
    private Integer idleTimeout;
    private Integer maxLifetime;

    /* -------------------------------
       Security / SSL
    ------------------------------- */
    private Boolean sslEnabled;
    private String sslCertPath;

    /* -------------------------------
       Metadata
    ------------------------------- */
    private String environment;       // dev, stg, prod
    private String description;

    @Builder.Default
    private boolean active = true;


}


