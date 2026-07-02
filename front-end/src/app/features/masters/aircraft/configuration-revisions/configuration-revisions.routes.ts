import { Routes } from '@angular/router';

export const AIRCRAFTCONFIGURATIONREVISION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/configuration-revisions-list/configuration-revisions-list.page').then((m) => m.AircraftConfigurationRevisionListPage) },
    { path: 'create', loadComponent: () => import('./pages/configuration-revisions-form/configuration-revisions-form.page').then((m) => m.AircraftConfigurationRevisionFormPage) },
    { path: ':id', loadComponent: () => import('./pages/configuration-revisions-form/configuration-revisions-form.page').then((m) => m.AircraftConfigurationRevisionFormPage) }
];
