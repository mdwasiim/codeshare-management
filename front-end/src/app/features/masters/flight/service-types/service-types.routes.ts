import { Routes } from '@angular/router';

export const FLIGHTSERVICETYPE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/service-types-list/service-types-list.page').then((m) => m.FlightServiceTypeListPage) },
    { path: 'create', loadComponent: () => import('./pages/service-types-form/service-types-form.page').then((m) => m.FlightServiceTypeFormPage) },
    { path: ':id', loadComponent: () => import('./pages/service-types-form/service-types-form.page').then((m) => m.FlightServiceTypeFormPage) }
];
