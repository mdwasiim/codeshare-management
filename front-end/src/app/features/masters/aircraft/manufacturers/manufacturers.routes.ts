import { Routes } from '@angular/router';

export const AIRCRAFTMANUFACTURER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/manufacturers-list/manufacturers-list.page').then((m) => m.AircraftManufacturerListPage) },
    { path: 'create', loadComponent: () => import('./pages/manufacturers-form/manufacturers-form.page').then((m) => m.AircraftManufacturerFormPage) },
    { path: ':id', loadComponent: () => import('./pages/manufacturers-form/manufacturers-form.page').then((m) => m.AircraftManufacturerFormPage) }
];
