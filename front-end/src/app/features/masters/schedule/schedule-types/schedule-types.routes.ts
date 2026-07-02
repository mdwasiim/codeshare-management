import { Routes } from '@angular/router';

export const SCHEDULETYPE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-types-list/schedule-types-list.page').then((m) => m.ScheduleTypeListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-types-form/schedule-types-form.page').then((m) => m.ScheduleTypeFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-types-form/schedule-types-form.page').then((m) => m.ScheduleTypeFormPage) }
];
