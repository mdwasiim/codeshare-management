import { Routes } from '@angular/router';

export const ACTIONCODE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/action-codes-list/action-codes-list.page').then((m) => m.ActionCodeListPage) },
    { path: 'create', loadComponent: () => import('./pages/action-codes-form/action-codes-form.page').then((m) => m.ActionCodeFormPage) },
    { path: ':id', loadComponent: () => import('./pages/action-codes-form/action-codes-form.page').then((m) => m.ActionCodeFormPage) }
];
