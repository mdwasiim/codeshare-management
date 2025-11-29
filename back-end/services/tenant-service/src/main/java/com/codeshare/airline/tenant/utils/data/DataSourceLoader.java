package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.common.utils.UuidUtil;
import com.codeshare.airline.tenant.entities.DataSource;
import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataSourceLoader {

    private final TenantDataSourceRepository dataSourceRepo;

    // -------------------------------------------------------------------------
    //  GLOBAL MASTER DATASOURCES (Safe Maps - No Map.of)
    // -------------------------------------------------------------------------
    private static final List<Map<String, Object>> MASTER_DS = new ArrayList<>();

    static {
        MASTER_DS.add(ds(
                "DATASOURCE_PRIMARY_MYSQL", "MYSQL", "PRIMARY",
                "com.mysql.cj.jdbc.Driver", "org.hibernate.dialect.MySQLDialect",
                "jdbc:mysql://db-primary.example.com:3306/primary_db",
                "root", "rootpass",
                "db-primary.example.com", 3306, "primary_db",
                "prod", "Primary MySQL database"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_PRIMARY_POSTGRES", "POSTGRES", "PRIMARY",
                "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect",
                "jdbc:postgresql://db-primary-pg.example.com:5432/primary_pg",
                "pg_user", "pg_pass",
                "db-primary-pg.example.com", 5432, "primary_pg",
                "prod", "Primary PostgreSQL database"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_SECONDARY_MARIADB", "MARIADB", "SECONDARY",
                "org.mariadb.jdbc.Driver", "org.hibernate.dialect.MariaDBDialect",
                "jdbc:mariadb://db-secondary.example.com:3306/secondary",
                "sec", "sec_pass",
                "db-secondary.example.com", 3306, "secondary",
                "prod", "MariaDB secondary failover"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_SECONDARY_MYSQL", "MYSQL", "SECONDARY",
                "com.mysql.cj.jdbc.Driver", "org.hibernate.dialect.MySQLDialect",
                "jdbc:mysql://mysql-secondary.example.com:3306/sec_db",
                "secmysql", "secpass",
                "mysql-secondary.example.com", 3306, "sec_db",
                "prod", "MySQL secondary backup"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_ANALYTICS_POSTGRES", "POSTGRES", "ANALYTICS",
                "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect",
                "jdbc:postgresql://analytics-db.example.com:5432/analytics",
                "analytics", "analytics_pass",
                "analytics-db.example.com", 5432, "analytics",
                "stg", "Analytics and BI PostgreSQL"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_ORACLE_BACKUP", "ORACLE", "BACKUP",
                "oracle.jdbc.OracleDriver", "org.hibernate.dialect.OracleDialect",
                "jdbc:oracle:thin:@//oracle-backup.example.com:1521/ORCL",
                "oracle", "oracle_pass",
                "oracle-backup.example.com", 1521, "ORCL",
                "prod", "Oracle backup database"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_SQLSERVER_DW", "SQLSERVER", "DATA_WAREHOUSE",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver", "org.hibernate.dialect.SQLServerDialect",
                "jdbc:sqlserver://sqlsrv-dw.example.com:1433;databaseName=dw",
                "dw", "dw_pass",
                "sqlsrv-dw.example.com", 1433, "dw",
                "prod", "SQL Server Data Warehouse"
        ));

        MASTER_DS.add(ds(
                "DATASOURCE_H2_LOCAL", "H2", "DEV",
                "org.h2.Driver", "org.hibernate.dialect.H2Dialect",
                "jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1",
                "sa", "sa",
                "localhost", 0, "devdb",
                "dev", "H2 in-memory development database"
        ));
    }

    // -------------------------------------------------------------------------
    // Helper to safely build datasource map
    // -------------------------------------------------------------------------
    private static Map<String, Object> ds(
            String key, String vendor, String type, String driver, String dialect,
            String url, String username, String password,
            String host, Integer port, String dbName,
            String env, String desc
    ) {
        Map<String, Object> m = new HashMap<>();
        m.put("key", key);
        m.put("vendor", vendor);
        m.put("type", type);
        m.put("driver", driver);
        m.put("dialect", dialect);
        m.put("url", url);
        m.put("username", username);
        m.put("password", password);
        m.put("host", host);
        m.put("port", port);
        m.put("databaseName", dbName);
        m.put("environment", env);
        m.put("description", desc);
        return m;
    }

    // -------------------------------------------------------------------------
    // Loader
    // -------------------------------------------------------------------------

    public void loadDatasorce() {

        if (dataSourceRepo.count() > 0) {
            System.out.println("✔ Datasources already present — skipping load.");
            return;
        }

        System.out.println("⏳ Loading global master datasources...");

        for (Map<String, Object> ds : MASTER_DS) {

            String key = (String) ds.get("key");
            UUID id = UuidUtil.fixed(key);  // deterministic UUID

            DataSource tds = DataSource.builder()
                    .id(id)
                    .dbVendor((String) ds.get("vendor"))
                    .dbType((String) ds.get("type"))
                    .driverClass((String) ds.get("driver"))
                    .dialect((String) ds.get("dialect"))
                    .dbUrl((String) ds.get("url"))
                    .username((String) ds.get("username"))
                    .password((String) ds.get("password"))

                    // extracted
                    .host((String) ds.get("host"))
                    .port((Integer) ds.get("port"))
                    .databaseName((String) ds.get("databaseName"))

                    // metadata
                    .environment((String) ds.get("environment"))
                    .description((String) ds.get("description"))
                    .active(true)
                    .build();

            dataSourceRepo.save(tds);
        }

        System.out.println("✔ Global datasources loaded successfully.");
    }
}
