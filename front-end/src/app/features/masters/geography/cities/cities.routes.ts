import { Routes } from '@angular/router';

export const CITY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/cities-list/cities-list.page').then((m) => m.CityListPage) },
    { path: 'create', loadComponent: () => import('./pages/cities-form/cities-form.page').then((m) => m.CityFormPage) },
    { path: ':id', loadComponent: () => import('./pages/cities-form/cities-form.page').then((m) => m.CityFormPage) }
];
