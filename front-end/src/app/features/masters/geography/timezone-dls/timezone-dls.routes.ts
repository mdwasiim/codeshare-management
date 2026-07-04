import { Routes } from '@angular/router';

export const TIMEZONE_DLS_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/timezone-dls-list/timezone-dls-list.page').then((m) => m.TimezoneDlsListPage) },
    { path: 'create', loadComponent: () => import('./pages/timezone-dls-form/timezone-dls-form.page').then((m) => m.TimezoneDlsFormPage) },
    { path: ':id', loadComponent: () => import('./pages/timezone-dls-form/timezone-dls-form.page').then((m) => m.TimezoneDlsFormPage) }
];
