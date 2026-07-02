import { Routes } from '@angular/router';

export const TRAFFICRESTRICTION_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/traffic-restrictions-list/traffic-restrictions-list.page').then((m) => m.TrafficRestrictionListPage) },
    { path: 'create', loadComponent: () => import('./pages/traffic-restrictions-form/traffic-restrictions-form.page').then((m) => m.TrafficRestrictionFormPage) },
    { path: ':id', loadComponent: () => import('./pages/traffic-restrictions-form/traffic-restrictions-form.page').then((m) => m.TrafficRestrictionFormPage) }
];
