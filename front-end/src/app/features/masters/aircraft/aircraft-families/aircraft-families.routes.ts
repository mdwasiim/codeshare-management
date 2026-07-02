import { Routes } from '@angular/router';

export const AIRCRAFTFAMILY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/aircraft-families-list/aircraft-families-list.page').then((m) => m.AircraftFamilyListPage) },
    { path: 'create', loadComponent: () => import('./pages/aircraft-families-form/aircraft-families-form.page').then((m) => m.AircraftFamilyFormPage) },
    { path: ':id', loadComponent: () => import('./pages/aircraft-families-form/aircraft-families-form.page').then((m) => m.AircraftFamilyFormPage) }
];
