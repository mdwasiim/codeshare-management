import { Routes } from '@angular/router';

export const FLIGHTSUFFIX_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/flight-suffixes-list/flight-suffixes-list.page').then((m) => m.FlightSuffixListPage) },
    { path: 'create', loadComponent: () => import('./pages/flight-suffixes-form/flight-suffixes-form.page').then((m) => m.FlightSuffixFormPage) },
    { path: ':id', loadComponent: () => import('./pages/flight-suffixes-form/flight-suffixes-form.page').then((m) => m.FlightSuffixFormPage) }
];
