import { Routes } from '@angular/router';

export const REGION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/regions-list/regions-list.page').then((m) => m.RegionListPage) },
    { path: 'create', loadComponent: () => import('./pages/regions-form/regions-form.page').then((m) => m.RegionFormPage) },
    { path: ':id', loadComponent: () => import('./pages/regions-form/regions-form.page').then((m) => m.RegionFormPage) }
];
