import { Routes } from '@angular/router';

export const UTCOFFSET_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/utc-offsets-list/utc-offsets-list.page').then((m) => m.UtcOffsetListPage) },
    { path: 'create', loadComponent: () => import('./pages/utc-offsets-form/utc-offsets-form.page').then((m) => m.UtcOffsetFormPage) },
    { path: ':id', loadComponent: () => import('./pages/utc-offsets-form/utc-offsets-form.page').then((m) => m.UtcOffsetFormPage) }
];
