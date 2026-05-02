import { Routes } from '@angular/router';


export const IAM_ROUTES: Routes = [
    {
        path: 'tenants',
        loadChildren: () =>
            import('@features/iam/tenants/tenant.routes')
                .then(m => m.TENANT_ROUTES)
    },

    {
        path: 'users',
        loadChildren: () =>
            import('@features/iam/users/users.routes')
                .then(m => m.USERS_ROUTES)
    },
    {
        path: 'roles',
        loadChildren: () =>
            import('@features/iam/roles/roles.routes')
                .then(m => m.ROLES_ROUTES)
    },
    {
        path: 'permissions',
        loadChildren: () =>
            import('@features/iam/permissions/permissions.routes')
                .then(m => m.PERMISSIONS_ROUTES)
    },
    {
        path: 'groups',
        loadChildren: () =>
            import('@features/iam/groups/groups.routes')
                .then(m => m.GROUPS_ROUTES)
    },
    {
        path: 'menus',
        loadChildren: () =>
            import('@features/iam/menus/menus.routes')
                .then(m => m.MENUS_ROUTES)
    }
];
