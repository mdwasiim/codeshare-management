import { Routes } from '@angular/router';

export const AUTHORIZATION_ROUTES: Routes = [
    {
        path: 'permissions',
        loadChildren: () => import('@features/access-management/authorization/permissions/permissions.routes').then((m) => m.PERMISSIONS_ROUTES)
    },
    {
        path: 'menus',
        loadChildren: () => import('@features/access-management/authorization/menus/menus.routes').then((m) => m.MENUS_ROUTES)
    }
];
