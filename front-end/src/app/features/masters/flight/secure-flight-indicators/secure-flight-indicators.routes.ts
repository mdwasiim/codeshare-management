import { Routes } from '@angular/router';

export const SECUREFLIGHTINDICATOR_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/secure-flight-indicators-list/secure-flight-indicators-list.page').then((m) => m.SecureFlightIndicatorListPage) },
    { path: 'create', loadComponent: () => import('./pages/secure-flight-indicators-form/secure-flight-indicators-form.page').then((m) => m.SecureFlightIndicatorFormPage) },
    { path: ':id', loadComponent: () => import('./pages/secure-flight-indicators-form/secure-flight-indicators-form.page').then((m) => m.SecureFlightIndicatorFormPage) }
];
