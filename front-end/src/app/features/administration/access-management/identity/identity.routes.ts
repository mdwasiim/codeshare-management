import { Routes } from '@angular/router';

export const IDENTITY_ROUTES: Routes = [
    {
        path: 'users',
        loadChildren: () => import('@features/administration/access-management/identity/users/users.routes').then((m) => m.USERS_ROUTES)
    },
    {
        path: 'roles',
        loadChildren: () => import('@features/administration/access-management/identity/roles/roles.routes').then((m) => m.ROLES_ROUTES)
    },
    {
        path: 'groups',
        loadChildren: () => import('@features/administration/access-management/identity/groups/groups.routes').then((m) => m.GROUPS_ROUTES)
    }
];

