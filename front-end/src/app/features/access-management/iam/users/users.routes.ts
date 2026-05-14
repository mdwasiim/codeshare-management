import { Routes } from '@angular/router';

export const USERS_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('./pages/user-list/user-list.page').then((m) => m.UserListPage)
    },
    {
        path: 'create',
        loadComponent: () => import('./pages/user-form/user-form.page').then((m) => m.UserFormPage)
    },
    {
        path: ':id',
        loadComponent: () => import('./pages/user-form/user-form.page').then((m) => m.UserFormPage)
    }
];
