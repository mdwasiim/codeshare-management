import { Routes } from '@angular/router';

export const AIRCRAFTCABINLAYOUT_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/cabin-layouts-list/cabin-layouts-list.page').then((m) => m.AircraftCabinLayoutListPage) },
    { path: 'create', loadComponent: () => import('./pages/cabin-layouts-form/cabin-layouts-form.page').then((m) => m.AircraftCabinLayoutFormPage) },
    { path: ':id', loadComponent: () => import('./pages/cabin-layouts-form/cabin-layouts-form.page').then((m) => m.AircraftCabinLayoutFormPage) }
];
