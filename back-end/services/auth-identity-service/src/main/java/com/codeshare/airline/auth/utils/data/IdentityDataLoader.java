package com.codeshare.airline.auth.utils.data;


import com.codeshare.airline.auth.entities.authorization.*;
import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.auth.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Auth RBAC DataLoader (tenantCode / organizationCode based)
 *
 * Reference tenant loader: /mnt/data/4a995fc6-05c3-4209-904b-294691c6406d.java
 *
 * NOTE: This loader does NOT use TenantRepository or OrganizationRepository
 * (auth-service must remain independent from tenant-service).
 */
@Component
@RequiredArgsConstructor
public class IdentityDataLoader {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final GroupRepository groupRepo;
    private final PermissionRepository permissionRepo;
    private final MenuRepository menuRepo;

    private final UserRoleRepository userRoleRepo;
    private final UserGroupRoleRepository userGroupRoleRepo;
    private final GroupRoleRepository groupRoleRepo;
    private final PermissionRoleRepository permissionRoleRepo;
    private final MenuRoleRepository menuRoleRepo;
    private final OrganizationRoleRepository organizationRoleRepo;

    @PostConstruct
    @Transactional
    public void load() throws JsonProcessingException {

        if (roleRepo.count() > 0 || permissionRepo.count() > 0 || userRepo.count() > 0) {
            System.out.println("RBAC initial data already present - skipping loader.");
            return;
        }

        System.out.println("Loading RBAC initial data (tenantCode-based) ...");

        // tenant codes (must match TenantDataLoader)
        List<String> tenantCodes = Arrays.asList("QR", "EK", "6E", "EY", "LH", "AI", "BA", "SQ", "AA", "AF");

        // ---------------------------
        // Permissions
        // ---------------------------
        List<Permission> permissions = Arrays.asList(
                Permission.builder().code("user:create").name("Create User").description("Create users").build(),
                Permission.builder().code("user:update").name("Update User").description("Update users").build(),
                Permission.builder().code("user:delete").name("Delete User").description("Delete users").build(),
                Permission.builder().code("role:create").name("Create Role").description("Create roles").build(),
                Permission.builder().code("group:create").name("Create Group").description("Create groups").build(),
                Permission.builder().code("flight:manage").name("Manage Flights").description("Full CRUD on flights").build(),
                Permission.builder().code("booking:manage").name("Manage Bookings").description("Create/modify bookings").build(),
                Permission.builder().code("finance:view").name("View Finance").description("View financial dashboards").build(),
                Permission.builder().code("audit:view").name("View Audit Logs").description("Access audit logs").build(),
                Permission.builder().code("reports:generate").name("Generate Reports").description("Run reports & BI").build()
        );
        permissionRepo.saveAll(permissions);
        Map<String, Permission> permMap = new HashMap<>();
        permissions.forEach(p -> permMap.put(p.getCode(), p));

        // ---------------------------
        // Menus
        // ---------------------------
 /*       List<Menu> menus = Arrays.asList(
                Menu.builder().code("DASHBOARD").title("Dashboard").url("/dashboard").orderIndex(10).build(),
                Menu.builder().code("USER_MGMT").title("User Management").url("/users").orderIndex(20).build(),
                Menu.builder().code("ROLE_MGMT").title("Roles").url("/roles").orderIndex(30).build(),
                Menu.builder().code("GROUP_MGMT").title("Groups").url("/groups").orderIndex(40).build(),
                Menu.builder().code("FLIGHTS").title("Flights").url("/flights").orderIndex(50).build(),
                Menu.builder().code("BOOKINGS").title("Bookings").url("/bookings").orderIndex(60).build(),
                Menu.builder().code("FINANCE").title("Finance").url("/finance").orderIndex(70).build(),
                Menu.builder().code("REPORTS").title("Reports").url("/reports").orderIndex(80).build(),
                Menu.builder().code("AUDIT").title("Audit").url("/audit").orderIndex(90).build()
        );
        menuRepo.saveAll(menus);
        Map<String, Menu> menuMap = new HashMap<>();
        menus.forEach(m -> menuMap.put(m.getCode(), m));*/


        // ----- MENUS -----
        String menuJson = "[{\"name\":\"Dashboard\",\"url\":\"/dashboard\",\"iconComponent\":{\"name\":\"cil-speedometer\"},\"badge\":{\"color\":\"info\",\"text\":\"NEW\"}},{\"title\":true,\"name\":\"Theme\"},{\"name\":\"Colors\",\"url\":\"/theme/colors\",\"iconComponent\":{\"name\":\"cil-drop\"}},{\"name\":\"Typography\",\"url\":\"/theme/typography\",\"iconComponent\":{\"name\":\"cil-pencil\"}},{\"name\":\"Components\",\"title\":true},{\"name\":\"Base\",\"url\":\"/base\",\"iconComponent\":{\"name\":\"cil-puzzle\"},\"children\":[{\"name\":\"Accordion\",\"url\":\"/base/accordion\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Breadcrumbs\",\"url\":\"/base/breadcrumbs\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Calendar\",\"url\":\"https://coreui.io/angular/docs/components/calendar/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"}},{\"name\":\"Cards\",\"url\":\"/base/cards\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Carousel\",\"url\":\"/base/carousel\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Collapse\",\"url\":\"/base/collapse\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"List Group\",\"url\":\"/base/list-group\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Navs & Tabs\",\"url\":\"/base/navs\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Pagination\",\"url\":\"/base/pagination\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Placeholder\",\"url\":\"/base/placeholder\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Smart Table\",\"url\":\"https://coreui.io/angular/docs/components/smart-table/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Smart Pagination\",\"url\":\"https://coreui.io/angular/docs/components/smart-pagination/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Popovers\",\"url\":\"/base/popovers\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Progress\",\"url\":\"/base/progress\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Spinners\",\"url\":\"/base/spinners\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Tables\",\"url\":\"/base/tables\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Tabs\",\"url\":\"/base/tabs\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Tooltips\",\"url\":\"/base/tooltips\",\"icon\":\"nav-icon-bullet\"}]},{\"name\":\"Buttons\",\"url\":\"/buttons\",\"iconComponent\":{\"name\":\"cil-cursor\"},\"children\":[{\"name\":\"Buttons\",\"url\":\"/buttons/buttons\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Button groups\",\"url\":\"/buttons/button-groups\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Dropdowns\",\"url\":\"/buttons/dropdowns\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Loading Button\",\"url\":\"https://coreui.io/angular/docs/components/loading-button/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}}]},{\"name\":\"Forms\",\"url\":\"/forms\",\"iconComponent\":{\"name\":\"cil-notes\"},\"children\":[{\"name\":\"Autocomplete\",\"url\":\"https://coreui.io/angular/docs/forms/autocomplete/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Form Control\",\"url\":\"/forms/form-control\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Checks & Radios\",\"url\":\"/forms/checks-radios\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Date Picker\",\"url\":\"https://coreui.io/angular/docs/forms/date-picker/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Date Range Picker\",\"url\":\"https://coreui.io/angular/docs/forms/date-range-picker/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Floating Labels\",\"url\":\"/forms/floating-labels\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Input Group\",\"url\":\"/forms/input-group\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Multi Select\",\"url\":\"https://coreui.io/angular/docs/forms/multi-select/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Password Input\",\"url\":\"https://coreui.io/angular/docs/forms/password-input/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Range\",\"url\":\"/forms/range\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Range Slider\",\"url\":\"https://coreui.io/angular/docs/forms/range-slider/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Rating\",\"url\":\"https://coreui.io/angular/docs/forms/rating/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Select\",\"url\":\"/forms/select\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Stepper\",\"url\":\"https://coreui.io/angular/docs/forms/stepper/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Time Picker\",\"url\":\"https://coreui.io/angular/docs/forms/time-picker/\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"danger\",\"text\":\"PRO\"},\"attributes\":{\"target\":\"_blank\"}},{\"name\":\"Layout\",\"url\":\"/forms/layout\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Validation\",\"url\":\"/forms/validation\",\"icon\":\"nav-icon-bullet\"}]},{\"name\":\"Charts\",\"iconComponent\":{\"name\":\"cil-chart-pie\"},\"url\":\"/charts\"},{\"name\":\"Icons\",\"iconComponent\":{\"name\":\"cil-star\"},\"url\":\"/icons\",\"children\":[{\"name\":\"CoreUI Free\",\"url\":\"/icons/coreui-icons\",\"icon\":\"nav-icon-bullet\",\"badge\":{\"color\":\"success\",\"text\":\"FREE\"}},{\"name\":\"CoreUI Flags\",\"url\":\"/icons/flags\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"CoreUI Brands\",\"url\":\"/icons/brands\",\"icon\":\"nav-icon-bullet\"}]},{\"name\":\"Notifications\",\"url\":\"/notifications\",\"iconComponent\":{\"name\":\"cil-bell\"},\"children\":[{\"name\":\"Alerts\",\"url\":\"/notifications/alerts\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Badges\",\"url\":\"/notifications/badges\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Modal\",\"url\":\"/notifications/modal\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Toast\",\"url\":\"/notifications/toasts\",\"icon\":\"nav-icon-bullet\"}]},{\"name\":\"Widgets\",\"url\":\"/widgets\",\"iconComponent\":{\"name\":\"cil-calculator\"},\"badge\":{\"color\":\"info\",\"text\":\"NEW\"}},{\"title\":true,\"name\":\"Extras\"},{\"name\":\"Pages\",\"url\":\"/login\",\"iconComponent\":{\"name\":\"cil-star\"},\"children\":[{\"name\":\"Login\",\"url\":\"/login\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Register\",\"url\":\"/register\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Error 404\",\"url\":\"/404\",\"icon\":\"nav-icon-bullet\"},{\"name\":\"Error 500\",\"url\":\"/500\",\"icon\":\"nav-icon-bullet\"}]},{\"title\":true,\"name\":\"Links\",\"class\":\"mt-auto\"},{\"name\":\"Docs\",\"url\":\"https://coreui.io/angular/docs/\",\"iconComponent\":{\"name\":\"cil-description\"},\"attributes\":{\"target\":\"_blank\"}}]";
        ObjectMapper objectMapper = new ObjectMapper();
        List<Menu> entities = objectMapper.readValue(menuJson, new TypeReference<List<Menu>>() {});

        for(Menu menus: entities ){
            if(menus.getChildren()!=null){
                List<Menu> children = menus.getChildren();
                for(Menu child: children){
                    child.setParentMenu(menus);
                }
            }

        }
        Map<String, Menu> menuMap = new HashMap<>();
        entities.forEach(m -> menuMap.put(m.getCode(), m));
        // ---------------------------
        // Roles
        // ---------------------------
        Role superAdmin = roleRepo.save(Role.builder().code("SUPER_ADMIN").name("Super Administrator").description("Full access to everything").build());
        Role tenantAdmin = roleRepo.save(Role.builder().code("TENANT_ADMIN").name("Tenant Administrator").description("Tenant-level admin").build());
        Role orgAdmin = roleRepo.save(Role.builder().code("ORG_ADMIN").name("Organization Administrator").description("Organization-level admin").build());
        Role manager = roleRepo.save(Role.builder().code("MANAGER").name("Manager").description("Manager role").build());
        Role staff = roleRepo.save(Role.builder().code("STAFF").name("Staff").description("Operational staff").build());

        // ----- MENU ROLES -----
        for(Menu menu: entities){
            MenuRole adminDashboardMenu = MenuRole.builder()
                    .menu(menu)
                    .role(superAdmin)
                    .build();
            MenuRole userSettingsMenu = MenuRole.builder()
                    .menu(menu)
                    .role(tenantAdmin)
                    .build();
            menu.setMenuRoles(new HashSet<>(Arrays.asList(adminDashboardMenu, userSettingsMenu)));
        }
        menuRepo.saveAll(entities);

        // ---------------------------
        // PermissionRole mappings
        // ---------------------------
        permissionRepo.findAll().forEach(p -> permissionRoleRepo.save(PermissionRole.builder().role(superAdmin).permission(p).build()));

        Arrays.asList("user:create","user:update","role:create","group:create","reports:generate","audit:view")
                .forEach(code -> permissionRoleRepo.save(PermissionRole.builder().role(tenantAdmin).permission(permMap.get(code)).build()));

        Arrays.asList("user:update","booking:manage")
                .forEach(code -> permissionRoleRepo.save(PermissionRole.builder().role(orgAdmin).permission(permMap.get(code)).build()));

        Arrays.asList("booking:manage","reports:generate")
                .forEach(code -> permissionRoleRepo.save(PermissionRole.builder().role(manager).permission(permMap.get(code)).build()));

        permissionRoleRepo.save(PermissionRole.builder().role(staff).permission(permMap.get("flight:manage")).build());

        // ---------------------------
        // MenuRole mappings
        // ---------------------------
        menuRepo.findAll().forEach(m -> menuRoleRepo.save(MenuRole.builder().role(superAdmin).menu(m).build()));

        Arrays.asList("USER_MGMT","ROLE_MGMT","GROUP_MGMT","REPORTS","AUDIT")
                .forEach(code -> menuRoleRepo.save(MenuRole.builder().role(tenantAdmin).menu(menuMap.get(code)).build()));

        Arrays.asList("USER_MGMT","BOOKINGS","FLIGHTS")
                .forEach(code -> menuRoleRepo.save(MenuRole.builder().role(orgAdmin).menu(menuMap.get(code)).build()));

        Arrays.asList("BOOKINGS","REPORTS")
                .forEach(code -> menuRoleRepo.save(MenuRole.builder().role(manager).menu(menuMap.get(code)).build()));

        Arrays.asList("FLIGHTS","BOOKINGS")
                .forEach(code -> menuRoleRepo.save(MenuRole.builder().role(staff).menu(menuMap.get(code)).build()));

        // ---------------------------
        // For each tenant create groups, users and mappings (tenantCode-based)
        // ---------------------------
        for (String code : tenantCodes) {

            // expected organization codes (must match TenantDataLoader)
            String hqOrgCode = code + "-HQ";
            String itOrgCode = code + "-IT";

            // -----------------
            // Groups (tenant-scoped)
            // -----------------
            Group adminGroup = groupRepo.save(Group.builder()
                    .code(code + "_ADMIN_GROUP")
                    .name(code + " Admin Group")
                    .description("Administrators for " + code)
                    .tenantId(UUID.fromString(code))         // tenantCode field (String)
                    
                    .build());

            Group itGroup = groupRepo.save(Group.builder()
                    .code(code + "_IT_GROUP")
                    .name(code + " IT Group")
                    .description("IT group for " + code)
                    .tenantId(UUID.fromString(code))

                    .build());

            Group opsGroup = groupRepo.save(Group.builder()
                    .code(code + "_OPS_GROUP")
                    .name(code + " Ops Group")
                    .description("Operations group for " + code)
                    .tenantId(UUID.fromString(code))

                    .build());

            // -----------------
            // GroupRole
            // -----------------
            groupRoleRepo.save(GroupRole.builder().group(adminGroup).role(tenantAdmin).build());
            groupRoleRepo.save(GroupRole.builder().group(itGroup).role(orgAdmin).build());
            groupRoleRepo.save(GroupRole.builder().group(opsGroup).role(staff).build());

            // -----------------
            // Users (tenant + org scoped using codes)
            // -----------------
            User admin = userRepo.save(User.builder()
                    .username(code.toLowerCase() + "_admin")
                    .email(code.toLowerCase() + "_admin@" + code.toLowerCase() + ".com")
                    .firstName("Admin")
                    .lastName(code)
                    .password("admin123")           // consider hashing
                    .tenantId(UUID.fromString(code))               // tenantCode field (String)
                    .organizationId(UUID.fromString(hqOrgCode))    // organizationCode field (String)
                    
                    .build());

            User manager1 = userRepo.save(User.builder()
                    .username(code.toLowerCase() + "_manager")
                    .email(code.toLowerCase() + "_manager@" + code.toLowerCase() + ".com")
                    .firstName("Manager")
                    .lastName(code)
                    .password("manager123")
                    .tenantId(UUID.fromString(code))
                    .organizationId(UUID.fromString(hqOrgCode))
                    
                    .build());

            User staffUser = userRepo.save(User.builder()
                    .username(code.toLowerCase() + "_staff")
                    .email(code.toLowerCase() + "_staff@" + code.toLowerCase() + ".com")
                    .firstName("Staff")
                    .lastName(code)
                    .password("staff123")
                    .tenantId(UUID.fromString(code))
                    .organizationId(UUID.fromString(itOrgCode))
                    
                    .build());

            // -----------------
            // UserRole (user->role with tenantCode)
            // -----------------
            userRoleRepo.save(UserRole.builder().user(admin).role(tenantAdmin).build());
            userRoleRepo.save(UserRole.builder().user(manager1).role(orgAdmin).build());
            userRoleRepo.save(UserRole.builder().user(staffUser).role(staff).build());

            // -----------------
            // UserGroupRole (user->group include tenantCode & organizationCode)
            // -----------------
            userGroupRoleRepo.save(UserGroupRole.builder()
                    .user(admin).group(adminGroup).build());

            userGroupRoleRepo.save(UserGroupRole.builder()
                    .user(manager1).group(itGroup).build());

            userGroupRoleRepo.save(UserGroupRole.builder()
                    .user(staffUser).group(opsGroup).build());

            // -----------------
            // OrganizationRole (link role to organization using codes)
            // -----------------
            organizationRoleRepo.save(OrganizationRole.builder()
                    .organizationId(UUID.fromString(hqOrgCode)).role(orgAdmin).build());
        }

        System.out.println("RBAC initial data load complete (auth-service) for all tenants.");
    }
}
