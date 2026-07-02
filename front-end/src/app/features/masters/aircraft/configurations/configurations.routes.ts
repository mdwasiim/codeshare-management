import { Routes } from '@angular/router';

export const AIRCRAFTCONFIGURATION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/configurations-list/configurations-list.page').then((m) => m.AircraftConfigurationListPage) },
    { path: 'create', loadComponent: () => import('./pages/configurations-form/configurations-form.page').then((m) => m.AircraftConfigurationFormPage) },
    { path: ':id', loadComponent: () => import('./pages/configurations-form/configurations-form.page').then((m) => m.AircraftConfigurationFormPage) }
];
