import { Routes } from '@angular/router';

export const TRAFFICCONFERENCEAREA_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/traffic-conference-areas-list/traffic-conference-areas-list.page').then((m) => m.TrafficConferenceAreaListPage) },
    { path: 'create', loadComponent: () => import('./pages/traffic-conference-areas-form/traffic-conference-areas-form.page').then((m) => m.TrafficConferenceAreaFormPage) },
    { path: ':id', loadComponent: () => import('./pages/traffic-conference-areas-form/traffic-conference-areas-form.page').then((m) => m.TrafficConferenceAreaFormPage) }
];
