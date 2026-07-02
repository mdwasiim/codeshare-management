import { Routes } from '@angular/router';

export const AIRCRAFTTYPE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/aircraft-types-list/aircraft-types-list.page').then((m) => m.AircraftTypeListPage) },
    { path: 'create', loadComponent: () => import('./pages/aircraft-types-form/aircraft-types-form.page').then((m) => m.AircraftTypeFormPage) },
    { path: ':id', loadComponent: () => import('./pages/aircraft-types-form/aircraft-types-form.page').then((m) => m.AircraftTypeFormPage) }
];
