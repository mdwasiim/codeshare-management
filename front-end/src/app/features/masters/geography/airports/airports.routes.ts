import { Routes } from '@angular/router';

export const AIRPORT_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/airports-list/airports-list.page').then((m) => m.AirportListPage) },
    { path: 'create', loadComponent: () => import('./pages/airports-form/airports-form.page').then((m) => m.AirportFormPage) },
    { path: ':id', loadComponent: () => import('./pages/airports-form/airports-form.page').then((m) => m.AirportFormPage) }
];
