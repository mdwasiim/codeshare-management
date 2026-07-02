import { Routes } from '@angular/router';

export const COUNTRY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/countries-list/countries-list.page').then((m) => m.CountryListPage) },
    { path: 'create', loadComponent: () => import('./pages/countries-form/countries-form.page').then((m) => m.CountryFormPage) },
    { path: ':id', loadComponent: () => import('./pages/countries-form/countries-form.page').then((m) => m.CountryFormPage) }
];
