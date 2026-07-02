import { Routes } from '@angular/router';

export const STATE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/states-list/states-list.page').then((m) => m.StateListPage) },
    { path: 'create', loadComponent: () => import('./pages/states-form/states-form.page').then((m) => m.StateFormPage) },
    { path: ':id', loadComponent: () => import('./pages/states-form/states-form.page').then((m) => m.StateFormPage) }
];
