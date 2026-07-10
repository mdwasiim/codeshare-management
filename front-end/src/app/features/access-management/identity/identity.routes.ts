import { Routes } from '@angular/router';

export const IDENTITY_ROUTES: Routes = [
    {
        path: 'tenants',
        loadChildren: () => import('@features/access-management/identity/tenants/tenants.routes').then((m) => m.TENANT_ROUTES)
    },
    {
        path: 'tenant-partners',
        loadChildren: () => import('@features/partner-management/tenant-partners/tenant-partners.routes').then((m) => m.TENANT_PARTNER_ROUTES)
    },

    {
        path: 'users',
        loadChildren: () => import('@features/access-management/identity/users/users.routes').then((m) => m.USERS_ROUTES)
    },
    {
        path: 'roles',
        loadChildren: () => import('@features/access-management/identity/roles/roles.routes').then((m) => m.ROLES_ROUTES)
    },
    {
        path: 'groups',
        loadChildren: () => import('@features/access-management/identity/groups/groups.routes').then((m) => m.GROUPS_ROUTES)
    }
];
