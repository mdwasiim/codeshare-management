package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.feign.client.TenantFeignClient;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final TenantFeignClient tenantFeignClient;

    private final GroupLoader groupLoader;
    private final GroupRoleLoader groupRoleLoader;
    private final MenuLoader menuLoader;
    private final MenuRoleLoader menuRoleLoader;
    private final PermissionLoader permissionLoader;
    private final PermissionRoleLoader permissionRoleLoader;
    private final RoleLoader roleLoader;
    private final UserGroupRoleLoader userGroupRoleLoader;
    private final UserLoader userLoader;
    private final UserRoleLoader userRoleLoader;

    @Override
    public void run(ApplicationArguments args) {

        log.info("⏳ AUTH-SERVICE: Fetching tenants from tenant-service...");

        List<String> tenantIds = fetchTenantIds();

        log.info("✔ Loaded {} tenants from tenant-service.", tenantIds.size());
        log.info("⏳ Starting AUTH-SERVICE RBAC data initialization...");

        tryLoad("Roles", () -> roleLoader.load(tenantIds));
        tryLoad("Permissions", () -> permissionLoader.load(tenantIds));
        tryLoad("Groups", () -> groupLoader.load(tenantIds));
        tryLoad("Menus", () -> menuLoader.load(tenantIds));
        tryLoad("Users", () -> userLoader.load(tenantIds));

        tryLoad("Group-Role Mapping", () -> groupRoleLoader.load(tenantIds));
        tryLoad("Menu-Role Mapping", () -> menuRoleLoader.load(tenantIds));
        tryLoad("Permission-Role Mapping", () -> permissionRoleLoader.load(tenantIds));
        tryLoad("User-Role Mapping", () -> userRoleLoader.load(tenantIds));
        tryLoad("User-Group Mapping", () -> userGroupRoleLoader.load(tenantIds));

        log.info("✔ AUTH-SERVICE initialization completed successfully.");
    }

    /**
     * Load tenant list from tenant-service via Feign.
     */
    private List<String> fetchTenantIds() {
        try {
            List<TenantDTO> tenants = tenantFeignClient.getAllTenants();
            return tenants.stream()
                    .map(t -> t.getId().toString())
                    .toList();
        } catch (Exception ex) {
            log.error("❌ Failed to load tenants from tenant-service: {}", ex.getMessage(), ex);
            throw new IllegalStateException("AUTH-SERVICE cannot start without tenants", ex);
        }
    }

    /**
     * Wrapper to isolate failures for each loader.
     */
    private void tryLoad(String name, LoaderOp op) {
        try {
            log.info("→ Loading {}...", name);
            op.run();
            log.info("✔ {} loaded.", name);
        } catch (Exception ex) {
            log.error("❌ Failed to load {}: {}", name, ex.getMessage(), ex);
        }
    }

    @FunctionalInterface
    interface LoaderOp {
        void run() throws Exception;
    }
}
