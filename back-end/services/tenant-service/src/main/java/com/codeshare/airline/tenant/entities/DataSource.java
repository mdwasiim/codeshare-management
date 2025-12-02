package com.codeshare.airline.tenant.entities;

import com.codeshare.airline.common.jpa.audit.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "mst_data_source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DataSource extends AbstractEntity {

    /* -------------------------------
       Core DB Definition
    ------------------------------- */
    @Column(name = "db_vendor", nullable = false)
    private String dbVendor;

    @Column(name = "db_type", nullable = false)
    private String dbType;

    @Column(name = "driver_class")
    private String driverClass;

    @Column(name = "dialect")
    private String dialect;

    /* -------------------------------
       Connection Details
    ------------------------------- */
    @Column(name = "db_url", nullable = false, length = 500)
    private String dbUrl;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private Integer port;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "connection_params")
    private String connectionParams;

    /* -------------------------------
       Pooling Settings
    ------------------------------- */
    @Column(name = "pool_name")
    private String poolName;

    @Column(name = "max_pool_size")
    private Integer maxPoolSize;

    @Column(name = "min_idle")
    private Integer minIdle;

    @Column(name = "connection_timeout")
    private Integer connectionTimeout;

    @Column(name = "idle_timeout")
    private Integer idleTimeout;

    @Column(name = "max_lifetime")
    private Integer maxLifetime;

    /* -------------------------------
       Security / SSL
    ------------------------------- */
    @Column(name = "ssl_enabled")
    private Boolean sslEnabled;

    @Column(name = "ssl_cert_path")
    private String sslCertPath;

    /* -------------------------------
       Metadata
    ------------------------------- */
    @Column(name = "environment")
    private String environment;

    @Column(name = "description")
    private String description;

    @Builder.Default
    @Column(name = "active")
    private boolean active = true;

}
