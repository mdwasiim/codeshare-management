import { Routes } from '@angular/router';

export const FLIGHTFREQUENCY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/flight-frequencies-list/flight-frequencies-list.page').then((m) => m.FlightFrequencyListPage) },
    { path: 'create', loadComponent: () => import('./pages/flight-frequencies-form/flight-frequencies-form.page').then((m) => m.FlightFrequencyFormPage) },
    { path: ':id', loadComponent: () => import('./pages/flight-frequencies-form/flight-frequencies-form.page').then((m) => m.FlightFrequencyFormPage) }
];
