import { Routes } from '@angular/router';

export const IAM_ROUTES: Routes = [
    {
        path: 'tenants',
        loadChildren: () => import('@features/access-management/iam/tenants/tenants.routes').then((m) => m.TENANT_ROUTES)
    },

    {
        path: 'users',
        loadChildren: () => import('@features/access-management/iam/users/users.routes').then((m) => m.USERS_ROUTES)
    },
    {
        path: 'roles',
        loadChildren: () => import('@features/access-management/iam/roles/roles.routes').then((m) => m.ROLES_ROUTES)
    },
    {
        path: 'permissions',
        loadChildren: () => import('@features/access-management/iam/permissions/permissions.routes').then((m) => m.PERMISSIONS_ROUTES)
    },
    {
        path: 'groups',
        loadChildren: () => import('@features/access-management/iam/groups/groups.routes').then((m) => m.GROUPS_ROUTES)
    },
    {
        path: 'menus',
        loadChildren: () => import('@features/access-management/iam/menus/menus.routes').then((m) => m.MENUS_ROUTES)
    }
];
