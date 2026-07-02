import { Routes } from '@angular/router';

export const TRAFFICRESTRICTIONQUALIFIER_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/traffic-restriction-qualifiers-list/traffic-restriction-qualifiers-list.page').then((m) => m.TrafficRestrictionQualifierListPage) },
    { path: 'create', loadComponent: () => import('./pages/traffic-restriction-qualifiers-form/traffic-restriction-qualifiers-form.page').then((m) => m.TrafficRestrictionQualifierFormPage) },
    { path: ':id', loadComponent: () => import('./pages/traffic-restriction-qualifiers-form/traffic-restriction-qualifiers-form.page').then((m) => m.TrafficRestrictionQualifierFormPage) }
];
