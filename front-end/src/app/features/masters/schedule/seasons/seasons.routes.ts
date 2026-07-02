import { Routes } from '@angular/router';

export const SEASON_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/seasons-list/seasons-list.page').then((m) => m.SeasonListPage) },
    { path: 'create', loadComponent: () => import('./pages/seasons-form/seasons-form.page').then((m) => m.SeasonFormPage) },
    { path: ':id', loadComponent: () => import('./pages/seasons-form/seasons-form.page').then((m) => m.SeasonFormPage) }
];
