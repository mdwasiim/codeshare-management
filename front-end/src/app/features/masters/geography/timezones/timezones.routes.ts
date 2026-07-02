import { Routes } from '@angular/router';

export const TIMEZONE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/timezones-list/timezones-list.page').then((m) => m.TimezoneListPage) },
    { path: 'create', loadComponent: () => import('./pages/timezones-form/timezones-form.page').then((m) => m.TimezoneFormPage) },
    { path: ':id', loadComponent: () => import('./pages/timezones-form/timezones-form.page').then((m) => m.TimezoneFormPage) }
];
