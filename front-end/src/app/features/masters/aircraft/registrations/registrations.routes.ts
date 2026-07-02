import { Routes } from '@angular/router';

export const AIRCRAFTREGISTRATION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/registrations-list/registrations-list.page').then((m) => m.AircraftRegistrationListPage) },
    { path: 'create', loadComponent: () => import('./pages/registrations-form/registrations-form.page').then((m) => m.AircraftRegistrationFormPage) },
    { path: ':id', loadComponent: () => import('./pages/registrations-form/registrations-form.page').then((m) => m.AircraftRegistrationFormPage) }
];
