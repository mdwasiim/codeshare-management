import {
    Routes
} from '@angular/router';

export const ACCESS_MANAGEMENT_ROUTES: Routes = [
    {
        path: 'role-permissions',

        loadComponent: () =>
            import(
                '../access-management/role-permissions/pages/role-permission-assignment.page'
                ).then(
                m => m.RolePermissionAssignmentPage
            )
    },

    {
        path: 'group-roles',

        loadComponent: () =>
            import(
                '../access-management/group-roles/pages/group-role-assignment.page'
                ).then(
                m => m.GroupRoleAssignmentPage
            )
    }

];
