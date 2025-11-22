package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.tenant.entities.Organization;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantDataSource;
import com.codeshare.airline.tenant.entities.UserGroup;
import com.codeshare.airline.tenant.repository.OrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.UserGroupRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TenantDataLoader {

    private final TenantRepository tenantRepo;
    private final TenantDataSourceRepository dataSourceRepo;
    private final OrganizationRepository orgRepo;
    private final UserGroupRepository userGroupRepo;

    @PostConstruct
    public void loadData() {

        if (tenantRepo.count() > 0) {
            System.out.println("Initial data already loaded.");
            return;
        }

        // ---------------------------------------------------------
        // 1️⃣ Tenants (Realistic Airline Sample)
        // ---------------------------------------------------------
        List<Tenant> tenants = Arrays.asList(
                Tenant.builder().code("QR").name("Qatar Airways").region("Qatar").plan("ENTERPRISE").enabled(true).build(),
                Tenant.builder().code("EK").name("Emirates Airlines").region("UAE").plan("ENTERPRISE").enabled(true).build(),
                Tenant.builder().code("6E").name("Indigo Airlines").region("India").plan("PRO").enabled(true).build(),
                Tenant.builder().code("EY").name("Etihad Airways").region("UAE").plan("PRO").enabled(true).build(),
                Tenant.builder().code("LH").name("Lufthansa").region("Germany").plan("ENTERPRISE").enabled(true).build(),

                // 🆕 New Tenants
                Tenant.builder().code("AI").name("Air India").region("India").plan("PRO").enabled(true).build(),
                Tenant.builder().code("BA").name("British Airways").region("UK").plan("ENTERPRISE").enabled(true).build(),
                Tenant.builder().code("SQ").name("Singapore Airlines").region("Singapore").plan("ENTERPRISE").enabled(true).build(),
                Tenant.builder().code("AA").name("American Airlines").region("USA").plan("PRO").enabled(true).build(),
                Tenant.builder().code("AF").name("Air France").region("France").plan("ENTERPRISE").enabled(true).build()
        );

        tenants.forEach(tenantRepo::save);

        tenants.forEach(tenantRepo::save);

        // ---------------------------------------------------------
        // 2️⃣ ALL POSSIBLE DATASOURCES FOR EACH TENANT
        // ---------------------------------------------------------
        tenants.forEach(tenant -> {

            // PostgreSQL
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("POSTGRES")
                    .dbType("PRIMARY")
                    .driverClass("org.postgresql.Driver")
                    .dialect("org.hibernate.dialect.PostgreSQLDialect")
                    .dbUrl("jdbc:postgresql://localhost:5432/" + tenant.getCode().toLowerCase() + "_db")
                    .username(tenant.getCode().toLowerCase() + "_user")
                    .password("Pass123$")
                    .active(true)
                    .build()
            );

            // MySQL
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("MYSQL")
                    .dbType("PRIMARY")
                    .driverClass("com.mysql.cj.jdbc.Driver")
                    .dialect("org.hibernate.dialect.MySQLDialect")
                    .dbUrl("jdbc:mysql://localhost:3306/" + tenant.getCode().toLowerCase() + "_db")
                    .username(tenant.getCode().toLowerCase() + "_user")
                    .password("Pass123$")
                    .active(true)
                    .build()
            );

            // MariaDB
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("MARIADB")
                    .dbType("PRIMARY")
                    .driverClass("org.mariadb.jdbc.Driver")
                    .dialect("org.hibernate.dialect.MariaDBDialect")
                    .dbUrl("jdbc:mariadb://localhost:3306/" + tenant.getCode().toLowerCase() + "_db")
                    .username(tenant.getCode().toLowerCase() + "_user")
                    .password("Pass123$")
                    .active(true)
                    .build()
            );

            // SQL Server
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("MSSQL")
                    .dbType("PRIMARY")
                    .driverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                    .dialect("org.hibernate.dialect.SQLServerDialect")
                    .dbUrl("jdbc:sqlserver://localhost:1433;databaseName=" + tenant.getCode().toLowerCase() + "_db")
                    .username(tenant.getCode().toLowerCase() + "_user")
                    .password("Pass123$")
                    .active(true)
                    .build()
            );

            // Oracle
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("ORACLE")
                    .dbType("PRIMARY")
                    .driverClass("oracle.jdbc.OracleDriver")
                    .dialect("org.hibernate.dialect.OracleDialect")
                    .dbUrl("jdbc:oracle:thin:@localhost:1521:" + tenant.getCode())
                    .username(tenant.getCode().toLowerCase() + "_user")
                    .password("Pass123$")
                    .active(true)
                    .build()
            );

            // H2
            dataSourceRepo.save(TenantDataSource.builder()
                    .tenant(tenant)
                    .dbVendor("H2")
                    .dbType("PRIMARY")
                    .driverClass("org.h2.Driver")
                    .dialect("org.hibernate.dialect.H2Dialect")
                    .dbUrl("jdbc:h2:mem:" + tenant.getCode().toLowerCase() + "_db")
                    .username("sa")
                    .password("")
                    .active(true)
                    .build()
            );
        });

        // ---------------------------------------------------------
        // 3️⃣ ORGANIZATIONS for each Tenant
        // ---------------------------------------------------------
        tenants.forEach(tenant -> {

            Organization hq = orgRepo.save(Organization.builder()
                    .tenant(tenant).name("Headquarters").code(tenant.getCode() + "-HQ").active(true).build());

            Organization it = orgRepo.save(Organization.builder()
                    .tenant(tenant).name("IT Department").code(tenant.getCode() + "-IT").parent(hq).active(true).build());

            Organization ops = orgRepo.save(Organization.builder()
                    .tenant(tenant).name("Operations").code(tenant.getCode() + "-OPS").parent(hq).active(true).build());

            Organization fin = orgRepo.save(Organization.builder()
                    .tenant(tenant).name("Finance").code(tenant.getCode() + "-FIN").parent(hq).active(true).build());

            Organization hr = orgRepo.save(Organization.builder()
                    .tenant(tenant).name("Human Resources").code(tenant.getCode() + "-HR").parent(hq).active(true).build());

            // ---------------------------------------------------------
            // 4️⃣ USER GROUPS for these organizations
            // ---------------------------------------------------------
            List<String> groups = Arrays.asList("Admin", "Manager", "Staff", "Developer", "Auditor");

            List<Organization> orgs = Arrays.asList(hq, it, ops, fin, hr);

            orgs.forEach(o -> {
                groups.forEach(gname -> {
                    userGroupRepo.save(UserGroup.builder()
                            .tenant(tenant)
                            .organization(o)
                            .name(gname + " Group")
                            .description("Group: " + gname + " for org " + o.getCode())
                            .active(true)
                            .build());
                });
            });
        });

        System.out.println("---------------------------------------------------");
        System.out.println("🔥 All tenants, organizations, user groups & ALL datasources loaded!");
        System.out.println("---------------------------------------------------");
    }
}


